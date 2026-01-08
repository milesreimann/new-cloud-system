package io.github.milesreimann.cloudsystem.k8s.server;

import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.model.ServerStatus;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import io.github.milesreimann.cloudsystem.master.domain.entity.ServerImpl;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 08.01.2026
 */
public class K8sServerDeployment implements ServerDeploymentPort {
    private final KubernetesClient kubernetesClient;

    public K8sServerDeployment(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public CompletableFuture<Server> deployServer(Node targetNode, ServerTemplate serverTemplate) {
        return CompletableFuture.supplyAsync(() -> {
            if (targetNode.getStatus() != NodeStatus.READY) {
                throw new IllegalStateException("Target node is not READY: " + targetNode.getName());
            }

            EnvVar type = new EnvVar("TYPE", "PAPER", null);
            EnvVar version = new EnvVar("VERSION", "1.21.8", null);
            EnvVar eula = new EnvVar("EULA", "TRUE", null);

            // TODO: Docker Image for ServerGroup or ServerTemplate, add resources

            Pod pod = new PodBuilder()
                .withNewMetadata()
                .withName(serverTemplate.getAbbreviation().toLowerCase(Locale.ROOT) + "-" + System.currentTimeMillis())
                .addToLabels("app", serverTemplate.getAbbreviation().toLowerCase(Locale.ROOT))
                .addToLabels("cloudsystem.io/node", targetNode.getName())
                .endMetadata()
                .withNewSpec()
                .addToContainers(new ContainerBuilder()
                    .withName(serverTemplate.getAbbreviation().toLowerCase(Locale.ROOT))
                    .withImage("itzg/minecraft-server")
                    .addToEnv(type)
                    .addToEnv(version)
                    .addToEnv(eula)
                    .build()
                )
                .withNodeName(targetNode.getName())
                .endSpec()
                .build();

            // TODO: Think about separate namespaces for groups / templates.
            Pod createdPod = kubernetesClient.pods().inNamespace("default").create(pod);

            return new ServerImpl(
                UUID.randomUUID(),
                1L,
                serverTemplate.getId(),
                ServerStatus.STARTING,
                Instant.now()
            );
        });
    }
}
