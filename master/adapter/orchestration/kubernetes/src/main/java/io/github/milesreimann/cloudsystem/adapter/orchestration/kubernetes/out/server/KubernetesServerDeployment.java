package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.server;

import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.DeploymentType;
import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.application.deployment.model.ArchiveFormat;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFileBundleDeployment;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.exception.MissingImageException;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.util.KubernetesStringSanitizer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 08.01.2026
 */
public class KubernetesServerDeployment implements ServerDeploymentPort {
    private static final String IMAGE_KEY = "image";
    private static final String RESOURCES_MEMORY = "memory";
    private static final String RESOURCES_CPU = "cpu";

    private static final String DATA_VOLUME_NAME = "server-data";
    private static final String DATA_MOUNT_PATH = "/data";

    private final KubernetesClient kubernetesClient;

    public KubernetesServerDeployment(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public CompletableFuture<Void> deployServer(Server server, ServerFileBundleDeployment bundleDeployment) {
        return CompletableFuture.runAsync(() -> {
            ServerTemplate serverTemplate = server.getTemplate();
            String sanitizedServerName = KubernetesStringSanitizer.sanitize(server.getName());
            String namespace = KubernetesStringSanitizer.sanitize(serverTemplate.getGroup().getName());

            String image = serverTemplate
                .getDeploymentMetadataValue(DeploymentType.KUBERNETES, IMAGE_KEY)
                .orElseThrow(() -> new MissingImageException("No image specified for server template: " + serverTemplate.getName()));

            List<EnvVar> environmentVariables = serverTemplate.getEnvironmentVariables().entrySet().stream()
                .map(entry -> new EnvVar(entry.getKey(), entry.getValue(), null))
                .toList();

            ResourceRequirements resourceRequirements = toKubernetesResources(serverTemplate);

            Pod pod = new PodBuilder()
                .withNewMetadata()
                .withName(sanitizedServerName)
                .endMetadata()
                .withNewSpec()
                .withNodeName(server.getNodeName())
                .addNewVolume()
                .withName(DATA_VOLUME_NAME)
                .withNewEmptyDir()
                .endEmptyDir()
                .endVolume()
                .addNewInitContainer()
                .withName("init-server-files")
                .withImage(initImageFor(bundleDeployment.format()))
                .withCommand("sh", "-c")
                .withArgs(buildInitCommand(bundleDeployment))
                .addNewVolumeMount()
                .withName(DATA_VOLUME_NAME)
                .withMountPath(DATA_MOUNT_PATH)
                .endVolumeMount()
                .endInitContainer()
                .addToContainers(new ContainerBuilder()
                    .withName(sanitizedServerName)
                    .withImage(image)
                    .addAllToEnv(environmentVariables)
                    .withResources(resourceRequirements)
                    .addNewVolumeMount()
                    .withName(DATA_VOLUME_NAME)
                    .withMountPath(DATA_MOUNT_PATH)
                    .endVolumeMount()
                    .build()
                )
                .endSpec()
                .build();

            kubernetesClient.pods().inNamespace(namespace).create(pod);
        });
    }

    private ResourceRequirements toKubernetesResources(ServerTemplate serverTemplate) {
        Resources requirements = serverTemplate.getRequirements();

        ResourceRequirementsBuilder builder = new ResourceRequirementsBuilder()
            .addToRequests(RESOURCES_CPU, new Quantity(requirements.getCpu().getMillicores() + requirements.getCpu().getSuffix()))
            .addToRequests(RESOURCES_MEMORY, toMemoryQuantity(requirements.getMemory()));

        Resources limits = serverTemplate.getLimits().orElse(requirements);

        builder
            .addToLimits(RESOURCES_CPU, new Quantity(limits.getCpu().getMillicores() + limits.getCpu().getSuffix()))
            .addToLimits(RESOURCES_MEMORY, toMemoryQuantity(limits.getMemory()));

        return builder.build();
    }

    private Quantity toMemoryQuantity(Memory memory) {
        return new Quantity(memory.getValue() + memory.getUnit().getSuffix());
    }

    private String initImageFor(ArchiveFormat format) {
        return switch (format) {
            case TAR_GZ -> "alpine:3.19";
            case ZIP -> "alpine:3.19";
        };
    }

    private String buildInitCommand(ServerFileBundleDeployment deployment) {
        String url = deployment.uri().toString();

        return switch (deployment.format()) {
            case TAR_GZ -> "set -e; "
                + "mkdir -p " + DATA_MOUNT_PATH + "; "
                + "wget -qO /tmp/bundle.tgz \"" + url + "\"; "
                + "tar -xzf /tmp/bundle.tgz -C " + DATA_MOUNT_PATH + ";";

            case ZIP -> "set -e; "
                + "mkdir -p " + DATA_MOUNT_PATH + "; "
                + "wget -qO /tmp/bundle.zip \"" + url + "\"; "
                + "unzip -oq /tmp/bundle.zip -d " + DATA_MOUNT_PATH + ";";
        };
    }
}