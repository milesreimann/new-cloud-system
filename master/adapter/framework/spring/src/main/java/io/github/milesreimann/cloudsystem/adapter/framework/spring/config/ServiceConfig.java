package io.github.milesreimann.cloudsystem.adapter.framework.spring.config;

import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.cache.ServerCache;
import io.github.milesreimann.cloudsystem.application.cache.ServerTemplateCache;
import io.github.milesreimann.cloudsystem.application.deployment.FilesBundler;
import io.github.milesreimann.cloudsystem.application.port.in.monitoring.RefreshNodeUsageUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.node.GetNodeUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.node.InitializeNodesUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.node.ListNodesUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.server.DeployServerUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.server.ScheduleServersUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.template.GetServerTemplateUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.template.ListServerTemplatesUseCase;
import io.github.milesreimann.cloudsystem.application.port.out.FileDeploymentPort;
import io.github.milesreimann.cloudsystem.application.port.out.FileLoaderPort;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProviderPort;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcherPort;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import io.github.milesreimann.cloudsystem.application.port.out.ServerTemplateRepository;
import io.github.milesreimann.cloudsystem.application.scheduling.NodeScheduler;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter.NodeStatusStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter.RequiredLabelStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter.ResourceAvailabilityStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.scoring.LeastLoadedScore;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.scoring.PreferredLabelScore;
import io.github.milesreimann.cloudsystem.application.usecase.monitoring.RefreshNodeUsageUseCaseImpl;
import io.github.milesreimann.cloudsystem.application.usecase.node.GetNodeUseCaseImpl;
import io.github.milesreimann.cloudsystem.application.usecase.node.InitializeNodesUseCaseImpl;
import io.github.milesreimann.cloudsystem.application.usecase.node.ListNodesUseCaseImpl;
import io.github.milesreimann.cloudsystem.application.usecase.node.NodeResourceReservationService;
import io.github.milesreimann.cloudsystem.application.usecase.server.DeployServerUseCaseImpl;
import io.github.milesreimann.cloudsystem.application.usecase.server.ScheduleServersUseCaseImpl;
import io.github.milesreimann.cloudsystem.application.usecase.server.ServerCreator;
import io.github.milesreimann.cloudsystem.application.usecase.template.ServerTemplateQueryService;
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
    public GetNodeUseCase getNodeUseCase(NodeCache nodeCache) {
        return new GetNodeUseCaseImpl(nodeCache);
    }

    @Bean
    public ListNodesUseCase listNodesUseCase(NodeCache nodeCache) {
        return new ListNodesUseCaseImpl(nodeCache);
    }

    @Bean
    public GetServerTemplateUseCase getServerTemplateUseCase(ServerTemplateQueryService serverTemplateQueryService) {
        return serverTemplateQueryService;
    }

    @Bean
    public ListServerTemplatesUseCase listServerTemplatesUseCase(ServerTemplateQueryService serverTemplateQueryService) {
        return serverTemplateQueryService;
    }

    @Bean
    public ServerTemplateQueryService serverTemplateQueryService(
        ServerTemplateRepository serverTemplateRepository,
        ServerTemplateCache serverTemplateCache
    ) {
        return new ServerTemplateQueryService(serverTemplateRepository, serverTemplateCache);
    }

    @Bean
    public ServerCreator serverCreator(ServerCache serverCache) {
        return new ServerCreator(serverCache);
    }

    @Bean
    public NodeResourceReservationService nodeResourceReservationService() {
        return new NodeResourceReservationService();
    }

    @Bean
    public RefreshNodeUsageUseCase refreshNodeUsageUseCase(
        NodeCache nodeCache,
        NodeUsageProviderPort nodeUsageProviderPort
    ) {
        return new RefreshNodeUsageUseCaseImpl(nodeCache, nodeUsageProviderPort);
    }

    @Bean
    public InitializeNodesUseCase initializeNodesUseCase(
        NodeRepository nodeRepository,
        NodeWatcherPort nodeWatcherPort,
        NodeCache nodeCache,
        ScheduleServersUseCase scheduleServersUseCase
    ) {
        return new InitializeNodesUseCaseImpl(nodeRepository, nodeWatcherPort, nodeCache, scheduleServersUseCase);
    }

    @Bean
    public ScheduleServersUseCase scheduleServersUseCase(
        GetServerTemplateUseCase getServerTemplateUseCase,
        ListServerTemplatesUseCase listServerTemplatesUseCase,
        ServerCreator serverCreator,
        NodeScheduler nodeScheduler,
        ListNodesUseCase listNodesUseCase,
        DeployServerUseCase deployServerUseCase
    ) {
        return new ScheduleServersUseCaseImpl(
            getServerTemplateUseCase,
            listServerTemplatesUseCase,
            serverCreator,
            nodeScheduler,
            listNodesUseCase,
            deployServerUseCase
        );
    }

    @Bean
    public DeployServerUseCase deployServerUseCase(
        FilesBundler filesBundler,
        FileDeploymentPort fileDeploymentPort,
        ServerDeploymentPort serverDeploymentPort
    ) {
        return new DeployServerUseCaseImpl(filesBundler, fileDeploymentPort, serverDeploymentPort);
    }
}