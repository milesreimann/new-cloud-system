package io.github.milesreimann.cloudsystem.k8s.server;

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
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import io.github.milesreimann.cloudsystem.k8s.exception.MissingImageException;
import io.github.milesreimann.cloudsystem.k8s.util.K8sStringSanitizer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 08.01.2026
 */
public class K8sServerDeployment implements ServerDeploymentPort {
    private static final String IMAGE_KEY = "image";
    private static final String RESOURCES_MEMORY = "memory";
    private static final String RESOURCES_CPU = "cpu";

    private final KubernetesClient kubernetesClient;

    public K8sServerDeployment(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public CompletableFuture<Void> deployServer(Node targetNode, Server server) {
        return CompletableFuture.runAsync(() -> {
            if (targetNode.getStatus() != NodeStatus.READY) {
                throw new IllegalStateException("Target node is not READY: " + targetNode.getName());
            }

            ServerTemplate serverTemplate = server.getTemplate();
            String sanitizedServerName = K8sStringSanitizer.sanitize(server.getName());
            String namespace = K8sStringSanitizer.sanitize(serverTemplate.getGroup().getName());

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
                .withNodeName(targetNode.getName())
                .addToContainers(new ContainerBuilder()
                    .withName(sanitizedServerName)
                    .withImage(image)
                    .addAllToEnv(environmentVariables)
                    .withResources(resourceRequirements)
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
}
