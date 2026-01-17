package io.github.milesreimann.cloudsystem.adapter.framework.spring.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.resilience.NodeWatcherRetryPolicy;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProviderPort;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcherPort;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.mapper.KubernetesNodeMapper;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.node.KubernetesNodeRepository;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.node.KubernetesNodeUsageProvider;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.node.KubernetesNodeWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Configuration
public class NodeConfig {
    @Bean
    public NodeWatcherPort nodeWatcher(
        KubernetesNodeMapper nodeMapper,
        NodeCache nodeCache,
        NodeWatcherRetryPolicy nodeWatcherReconnectHandler,
        KubernetesClient kubernetesClient
    ) {
        return new KubernetesNodeWatcher(nodeMapper, nodeCache, nodeWatcherReconnectHandler, kubernetesClient);
    }

    @Bean
    public NodeUsageProviderPort nodeUsageProvider(KubernetesClient kubernetesClient, Executor executor) {
        return new KubernetesNodeUsageProvider(kubernetesClient, executor);
    }

    @Bean
    public NodeWatcherRetryPolicy nodeWatcherReconnectHandler() {
        return new NodeWatcherRetryPolicy(5, 5000L);
    }

    @Bean
    public KubernetesNodeMapper nodeMapper() {
        return new KubernetesNodeMapper();
    }

    @Bean
    public NodeRepository nodeRepository(KubernetesClient kubernetesClient, KubernetesNodeMapper nodeMapper) {
        return new KubernetesNodeRepository(kubernetesClient, nodeMapper);
    }
}
