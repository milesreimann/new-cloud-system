package io.github.milesreimann.cloudsystem.adapter.spring.config;

import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.cache.ServerCache;
import io.github.milesreimann.cloudsystem.application.cache.ServerTemplateCache;
import io.github.milesreimann.cloudsystem.application.port.out.FileDeploymentPort;
import io.github.milesreimann.cloudsystem.application.port.out.FileLoaderPort;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProviderPort;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcherPort;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import io.github.milesreimann.cloudsystem.application.port.out.ServerTemplateRepository;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.NodeStatusStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.RequiredLabelStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.ResourceAvailabilityStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.scoring.LeastLoadedScore;
import io.github.milesreimann.cloudsystem.application.scheduling.scoring.PreferredLabelScore;
import io.github.milesreimann.cloudsystem.application.service.NodeInitializationService;
import io.github.milesreimann.cloudsystem.application.service.NodeReservationService;
import io.github.milesreimann.cloudsystem.application.service.NodeService;
import io.github.milesreimann.cloudsystem.application.service.NodeUsageService;
import io.github.milesreimann.cloudsystem.application.service.ServerDeploymentService;
import io.github.milesreimann.cloudsystem.application.service.ServerSchedulerService;
import io.github.milesreimann.cloudsystem.application.service.ServerService;
import io.github.milesreimann.cloudsystem.application.service.ServerTemplateService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Configuration
public class ServiceConfig {
    @Bean
    public NodeService nodeService(NodeCache nodeCache) {
        return new NodeService(nodeCache);
    }

    @Bean
    public ServerTemplateService serverTemplateService(
        ServerTemplateRepository serverTemplateRepository,
        ServerTemplateCache serverTemplateCache
    ) {
        return new ServerTemplateService(serverTemplateRepository, serverTemplateCache);
    }

    @Bean
    public ServerService serverService(ServerCache serverCache) {
        return new ServerService(serverCache);
    }

    @Bean
    public NodeReservationService nodeReservationService() {
        return new NodeReservationService();
    }

    @Bean
    public NodeUsageService nodeUsageService(
        NodeCache nodeCache,
        NodeUsageProviderPort nodeUsageProviderPort,
        Executor nodeUsageExecutor
    ) {
        return new NodeUsageService(nodeCache, nodeUsageProviderPort, nodeUsageExecutor);
    }

    @Bean
    public NodeInitializationService nodeInitializationService(
        NodeRepository nodeRepository,
        NodeWatcherPort nodeWatcherPort,
        NodeCache nodeCache,
        io.github.milesreimann.cloudsystem.application.service.ServerSchedulerService serverSchedulerService
    ) {
        return new NodeInitializationService(nodeRepository, nodeWatcherPort, nodeCache, serverSchedulerService);
    }

    @Bean
    public ServerSchedulerService serverSchedulerService(
        NodeService nodeService,
        NodeReservationService nodeReservationService,
        ServerTemplateService serverTemplateService,
        ServerService serverService,
        ServerDeploymentService serverDeploymentService,
        ResourceAvailabilityStrategy resourceAvailabilityStrategy,
        RequiredLabelStrategy requiredLabelStrategy,
        NodeStatusStrategy nodeStatusStrategy,
        PreferredLabelScore preferredLabelScore,
        LeastLoadedScore leastLoadedScore
    ) {
        return new ServerSchedulerService(
            nodeService,
            nodeReservationService,
            serverTemplateService,
            serverService,
            serverDeploymentService,
            List.of(
                resourceAvailabilityStrategy,
                requiredLabelStrategy,
                nodeStatusStrategy
            ),
            List.of(
                preferredLabelScore,
                leastLoadedScore
            )
        );
    }

    @Bean
    public ServerDeploymentService serverDeploymentService(
        FileLoaderPort fileLoaderPort,
        FileDeploymentPort fileDeploymentPort,
        ServerDeploymentPort serverDeploymentPort
    ) {
        return new ServerDeploymentService(fileLoaderPort, fileDeploymentPort, serverDeploymentPort);
    }
}