package io.github.milesreimann.cloudsystem.master.adapter.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.service.NodeService;
import io.github.milesreimann.cloudsystem.application.service.NodeUsageService;
import io.github.milesreimann.cloudsystem.application.service.ServerSchedulingService;
import io.github.milesreimann.cloudsystem.application.scheduling.scoring.LeastLoadedScore;
import io.github.milesreimann.cloudsystem.application.scheduling.scoring.PreferredLabelScore;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.RequiredLabelStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.ResourceAvailabilityStrategy;
import io.github.milesreimann.cloudsystem.master.adapter.mapper.NodeMapper;
import io.github.milesreimann.cloudsystem.master.adapter.out.K8sNodeUsageProvider;
import io.github.milesreimann.cloudsystem.master.adapter.watcher.K8sNodeWatcher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
@Configuration
public class ApplicationBeans {
    @Bean
    public KubernetesClient kubernetesClient() {
        return new KubernetesClientBuilder().build();
    }

    @Bean
    public Executor nodeUsageExecutor() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean
    public NodeMapper nodeMapper() {
        return new NodeMapper();
    }

    @Bean
    public NodeCache nodeCache() {
        return new NodeCache();
    }

    @Bean
    public K8sNodeWatcher k8sNodeWatcher(
        ApplicationEventPublisher eventPublisher,
        NodeMapper nodeMapper,
        NodeCache nodeCache,
        KubernetesClient kubernetesClient
    ) {
        return new K8sNodeWatcher(
            eventPublisher,
            nodeMapper,
            nodeCache,
            kubernetesClient
        );
    }

    @Bean
    public K8sNodeUsageProvider k8sNodeUsageProvider(KubernetesClient kubernetesClient) {
        return new K8sNodeUsageProvider(kubernetesClient);
    }

    @Bean
    public NodeService nodeService(
        NodeCache nodeCache,
        K8sNodeWatcher nodeWatcher
    ) {
        return new NodeService(nodeCache, nodeWatcher);
    }

    @Bean
    public ServerSchedulingService serverSchedulingService(NodeService nodeService) {
        return new ServerSchedulingService(
            nodeService,
            List.of(
                new ResourceAvailabilityStrategy(1),
                new RequiredLabelStrategy()
            ),
            List.of(
                new PreferredLabelScore(),
                new LeastLoadedScore(0.6F, 0.4F)
            )
        );
    }

    @Bean
    public NodeUsageService nodeUsageService(
        NodeCache nodeCache,
        K8sNodeUsageProvider nodeUsageProvider,
        Executor nodeUsageExecutor
    ) {
        return new NodeUsageService(
            nodeCache,
            nodeUsageProvider,
            nodeUsageExecutor
        );
    }
}
