package io.github.milesreimann.cloudsystem.adapter.spring.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.handler.NodeWatcherReconnectHandler;
import io.github.milesreimann.cloudsystem.application.mapper.NodeMapper;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProviderPort;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcherPort;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeMapper;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeRepository;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeUsageProvider;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Configuration
public class NodeConfig {
    @Bean
    public NodeWatcherPort nodeWatcher(
        K8sNodeMapper nodeMapper,
        NodeCache nodeCache,
        NodeWatcherReconnectHandler nodeWatcherReconnectHandler,
        KubernetesClient kubernetesClient
    ) {
        return new K8sNodeWatcher(nodeMapper, nodeCache, nodeWatcherReconnectHandler, kubernetesClient);
    }

    @Bean
    public NodeUsageProviderPort nodeUsageProvider(KubernetesClient kubernetesClient) {
        return new K8sNodeUsageProvider(kubernetesClient);
    }

    @Bean
    public NodeWatcherReconnectHandler nodeWatcherReconnectHandler() {
        return new NodeWatcherReconnectHandler(5, 5000L);
    }

    @Bean
    public K8sNodeMapper nodeMapper() {
        return new K8sNodeMapper();
    }

    @Bean
    public NodeRepository nodeRepository(KubernetesClient kubernetesClient, K8sNodeMapper nodeMapper) {
        return new K8sNodeRepository(kubernetesClient, nodeMapper);
    }
}
