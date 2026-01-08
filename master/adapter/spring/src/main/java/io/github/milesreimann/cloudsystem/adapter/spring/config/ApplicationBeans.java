package io.github.milesreimann.cloudsystem.adapter.spring.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.github.milesreimann.cloudsystem.api.event.EventBus;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.cache.ServerCache;
import io.github.milesreimann.cloudsystem.application.cache.ServerTemplateCache;
import io.github.milesreimann.cloudsystem.application.event.SimpleEventBus;
import io.github.milesreimann.cloudsystem.application.handler.NodeWatcherReconnectHandler;
import io.github.milesreimann.cloudsystem.application.mapper.NodeMapper;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcherPort;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import io.github.milesreimann.cloudsystem.application.port.out.ServerTemplateRepository;
import io.github.milesreimann.cloudsystem.application.service.NodeInitializationService;
import io.github.milesreimann.cloudsystem.application.service.NodeService;
import io.github.milesreimann.cloudsystem.application.service.NodeUsageService;
import io.github.milesreimann.cloudsystem.application.service.ServerSchedulerService;
import io.github.milesreimann.cloudsystem.application.scheduling.scoring.LeastLoadedScore;
import io.github.milesreimann.cloudsystem.application.scheduling.scoring.PreferredLabelScore;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.RequiredLabelStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.ResourceAvailabilityStrategy;
import io.github.milesreimann.cloudsystem.application.service.ServerService;
import io.github.milesreimann.cloudsystem.application.service.ServerTemplateService;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeMapper;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeRepository;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeUsageProvider;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeWatcher;
import io.github.milesreimann.cloudsystem.k8s.server.K8sServerDeployment;
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
    public K8sNodeMapper nodeMapper() {
        return new K8sNodeMapper();
    }

    @Bean
    public NodeCache nodeCache() {
        return new NodeCache();
    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public K8sNodeWatcher k8sNodeWatcher(
        K8sNodeMapper nodeMapper,
        NodeCache nodeCache,
        NodeWatcherReconnectHandler nodeWatcherReconnectHandler,
        KubernetesClient kubernetesClient
    ) {
        return new K8sNodeWatcher(
            nodeMapper,
            nodeCache,
            nodeWatcherReconnectHandler,
            kubernetesClient
        );
    }

    @Bean
    public NodeInitializationService nodeInitializationService(
        NodeRepository nodeRepository,
        NodeWatcherPort nodeWatcherPort,
        NodeCache nodeCache,
        ServerSchedulerService serverSchedulerService
    ) {
        return new NodeInitializationService(nodeRepository, nodeWatcherPort, nodeCache, serverSchedulerService);
    }

    @Bean
    public K8sNodeUsageProvider k8sNodeUsageProvider(KubernetesClient kubernetesClient) {
        return new K8sNodeUsageProvider(kubernetesClient);
    }

    @Bean
    public NodeService nodeService(NodeCache nodeCache) {
        return new NodeService(nodeCache);
    }

    @Bean
    public ServerTemplateCache serverTemplateCache() {
        return new ServerTemplateCache();
    }

    @Bean
    public ServerTemplateService serverTemplateService(
        ServerTemplateRepository serverTemplateRepository,
        ServerTemplateCache serverTemplateCache
    ) {
        return new ServerTemplateService(serverTemplateRepository, serverTemplateCache);
    }

    @Bean
    public ServerCache serverCache() {
        return new ServerCache();
    }

    @Bean
    public ServerService serverService(ServerCache serverCache) {
        return new ServerService(serverCache);
    }

    @Bean
    public ServerSchedulerService serverSchedulingService(
        NodeService nodeService,
        ServerTemplateService serverTemplateService,
        ServerService serverService,
        ServerDeploymentPort serverDeploymentPort
    ) {
        return new ServerSchedulerService(
            nodeService,
            serverTemplateService,
            serverService,
            serverDeploymentPort,
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

    @Bean
    public ServerDeploymentPort serverDeploymentPort(KubernetesClient kubernetesClient) {
        return new K8sServerDeployment(kubernetesClient);
    }

    @Bean
    public NodeRepository nodeRepository(KubernetesClient kubernetesClient, K8sNodeMapper nodeMapper) {
        return new K8sNodeRepository(kubernetesClient, nodeMapper);
    }

    @Bean
    public NodeWatcherReconnectHandler nodeWatcherReconnectHandler() {
        return new NodeWatcherReconnectHandler(5, 5000L);
    }
}