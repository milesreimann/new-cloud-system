package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.server;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.pod.ServerPodBuilder;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.util.KubernetesStringSanitizer;
import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFileBundleDeployment;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author Miles R.
 * @since 08.01.2026
 */
public class KubernetesServerDeployment implements ServerDeploymentPort {
    private static final Logger LOG = LoggerFactory.getLogger(KubernetesServerDeployment.class);

    private final KubernetesClient kubernetesClient;
    private final ServerPodBuilder podBuilder;
    private final Executor executor;

    public KubernetesServerDeployment(
        KubernetesClient kubernetesClient,
        ServerPodBuilder podBuilder,
        Executor executor
    ) {
        this.kubernetesClient = kubernetesClient;
        this.podBuilder = podBuilder;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Void> deployServer(
        Server server,
        ServerFileBundleDeployment bundleDeployment
    ) {
        return CompletableFuture.runAsync(
            () -> createPodInKubernetes(server, bundleDeployment),
            executor
        );
    }

    private void createPodInKubernetes(Server server, ServerFileBundleDeployment bundleDeployment) {
        LOG.debug("Creating Kubernetes Pod for server '{}'", server.getName());

        Pod pod = podBuilder.buildPod(server, bundleDeployment);
        String namespace = KubernetesStringSanitizer.sanitize(server.getTemplate().getGroup().getName());

        kubernetesClient.pods()
            .inNamespace(namespace)
            .resource(pod)
            .create();

        LOG.info("Pod created for server '{}' in namespace '{}'", server.getName(), namespace);
    }
}