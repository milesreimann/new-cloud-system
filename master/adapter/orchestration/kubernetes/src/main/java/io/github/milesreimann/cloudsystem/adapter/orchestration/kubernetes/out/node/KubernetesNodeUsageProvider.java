package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.node;

import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetrics;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProviderPort;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.util.KubernetesMetricParser;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public class KubernetesNodeUsageProvider implements NodeUsageProviderPort {
    private final KubernetesClient kubernetesClient;
    private final Executor executor;

    public KubernetesNodeUsageProvider(KubernetesClient kubernetesClient, Executor executor) {
        this.kubernetesClient = kubernetesClient;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Resources> getUsage(String nodeName) {
        return CompletableFuture.supplyAsync(
            () -> {
                NodeMetrics metrics = kubernetesClient.top()
                    .nodes()
                    .metrics(nodeName);

                if (metrics == null || metrics.getUsage() == null) {
                    return ResourcesImpl.empty();
                }

                return KubernetesMetricParser.parseResources(metrics.getUsage());
            },
            executor
        );
    }
}