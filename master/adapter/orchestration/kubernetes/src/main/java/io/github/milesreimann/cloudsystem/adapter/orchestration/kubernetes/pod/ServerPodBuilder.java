package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.pod;

import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.exception.MissingImageException;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.mapper.ResourceRequirementsMapper;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.util.KubernetesStringSanitizer;
import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.DeploymentType;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFileBundleDeployment;

import java.util.List;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class ServerPodBuilder {
    public static final String METADATA_KEY_IMAGE = "image";

    public static final String DATA_VOLUME_NAME = "server-data";
    public static final String INIT_CONTAINER_NAME = "init-server-files";

    private final ResourceRequirementsMapper resourceRequirementsMapper;
    private final InitContainerSpec initContainerSpec;

    public ServerPodBuilder(
        ResourceRequirementsMapper resourceRequirementsMapper,
        InitContainerSpec initContainerSpec
    ) {
        this.resourceRequirementsMapper = resourceRequirementsMapper;
        this.initContainerSpec = initContainerSpec;
    }

    public Pod buildPod(Server server, ServerFileBundleDeployment bundleDeployment) {
        ServerTemplate template = server.getTemplate();

        String podName = KubernetesStringSanitizer.sanitize(server.getName());
        String namespace = KubernetesStringSanitizer.sanitize(template.getGroup().getName());
        String image = extractImage(template);

        List<EnvVar> envVars = buildEnvironmentVariables(template);
        ResourceRequirements resources = resourceRequirementsMapper.toKubernetesResources(template);

        return new PodBuilder()
            .withNewMetadata()
            .withName(podName)
            .withNamespace(namespace)
            .endMetadata()
            .withNewSpec()
            .withNodeName(server.getNodeName())
            .addNewVolume()
            .withName(DATA_VOLUME_NAME)
            .withNewEmptyDir()
            .endEmptyDir()
            .endVolume()
            .addNewInitContainer()
            .withName(INIT_CONTAINER_NAME)
            .withImage(initContainerSpec.getImageForFormat(bundleDeployment.format()))
            .withCommand("sh", "-c")
            .withArgs(initContainerSpec.buildDownloadAndExtractCommand(bundleDeployment))
            .addNewVolumeMount()
            .withName(DATA_VOLUME_NAME)
            .withMountPath(InitContainerSpec.DATA_MOUNT_PATH)
            .endVolumeMount()
            .endInitContainer()
            .addToContainers(new ContainerBuilder()
                .withName(podName)
                .withImage(image)
                .addAllToEnv(envVars)
                .withResources(resources)
                .addNewVolumeMount()
                .withName(DATA_VOLUME_NAME)
                .withMountPath(InitContainerSpec.DATA_MOUNT_PATH)
                .endVolumeMount()
                .build()
            )
            .endSpec()
            .build();
    }

    private String extractImage(ServerTemplate template) {
        return template
            .getDeploymentMetadataValue(DeploymentType.KUBERNETES, METADATA_KEY_IMAGE)
            .orElseThrow(() -> new MissingImageException("No image specified for server template: " + template.getName()));
    }

    private List<EnvVar> buildEnvironmentVariables(ServerTemplate template) {
        return template.getEnvironmentVariables().entrySet().stream()
            .map(entry -> new EnvVar(entry.getKey(), entry.getValue(), null))
            .toList();
    }
}