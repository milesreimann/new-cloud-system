package io.github.milesreimann.cloudsystem.application.usecase.server;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.exception.NoSuitableNodeException;
import io.github.milesreimann.cloudsystem.application.exception.ServerTemplateNotFoundException;
import io.github.milesreimann.cloudsystem.application.port.in.node.ListNodesUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.server.DeployServerUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.server.ScheduleServersUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.template.GetServerTemplateUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.template.ListServerTemplatesUseCase;
import io.github.milesreimann.cloudsystem.application.scheduling.NodeScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class ScheduleServersUseCaseImpl implements ScheduleServersUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleServersUseCaseImpl.class);

    private final GetServerTemplateUseCase getServerTemplateUseCase;
    private final ListServerTemplatesUseCase listServerTemplatesUseCase;
    private final ServerCreator serverCreator;
    private final NodeScheduler nodeScheduler;
    private final ListNodesUseCase listNodesUseCase;
    private final DeployServerUseCase deployServerUseCase;

    public ScheduleServersUseCaseImpl(
        GetServerTemplateUseCase getServerTemplateUseCase,
        ListServerTemplatesUseCase listServerTemplatesUseCase,
        ServerCreator serverCreator,
        NodeScheduler nodeScheduler,
        ListNodesUseCase listNodesUseCase,
        DeployServerUseCase deployServerUseCase
    ) {
        this.getServerTemplateUseCase = getServerTemplateUseCase;
        this.listServerTemplatesUseCase = listServerTemplatesUseCase;
        this.serverCreator = serverCreator;
        this.nodeScheduler = nodeScheduler;
        this.listNodesUseCase = listNodesUseCase;
        this.deployServerUseCase = deployServerUseCase;
    }

    @Override
    public CompletableFuture<List<Server>> scheduleAllActiveTemplates() {
        LOG.info("Starting scheduling for all active templates");

        List<ServerTemplate> activeTemplates = listServerTemplatesUseCase.listActive();
        if (activeTemplates.isEmpty()) {
            LOG.info("No active templates found, nothing to schedule");
            return CompletableFuture.completedFuture(List.of());
        }

        LOG.debug("Found {} active templates to schedule", activeTemplates.size());

        List<CompletableFuture<List<Server>>> futures = activeTemplates.stream()
            .map(template -> scheduleTemplate(template.getId())
                .exceptionally(t -> {
                    LOG.error("Failed to schedule template {}: {}", template.getName(), t.getMessage());
                    return List.of();
                })
            )
            .toList();

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
            .thenApply(_ -> {
                List<Server> servers = futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(Collection::stream)
                    .toList();

                LOG.info("Scheduling completed: {} servers started across {} templates",
                    servers.size(), activeTemplates.size());
                return servers;
            });
    }

    @Override
    public CompletableFuture<List<Server>> scheduleTemplate(long templateId) {
        return getServerTemplateUseCase.getById(templateId)
            .map(this::scheduleTemplateInternal)
            .orElseGet(() -> {
                LOG.warn("Template with id={} not found", templateId);
                return CompletableFuture.failedFuture(new ServerTemplateNotFoundException(templateId));
            });
    }

    private CompletableFuture<List<Server>> scheduleTemplateInternal(ServerTemplate template) {
        LOG.debug("Scheduling template: '{}'", template.getName());

        int serversToStart = calculateServersToStart(template);
        if (serversToStart <= 0) {
            return CompletableFuture.completedFuture(List.of());
        }

        LOG.info("Scheduling {} servers for template '{}' (current: {}, min: {}, max: {})",
            serversToStart,
            template.getName(),
            serverCreator.countServersForTemplate(template.getId()),
            template.getMinServers(),
            template.getMaxServers()
        );

        return startServers(template, serversToStart);
    }

    private int calculateServersToStart(ServerTemplate template) {
        if (template.getMinServers() <= 0) {
            LOG.trace("Template '{}' has no positive minServers, skipping", template.getName());
            return 0;
        }

        int currentCount = serverCreator.countServersForTemplate(template.getId());
        LOG.debug("Template '{}' currently has {} running servers", template.getName(), currentCount);

        if (currentCount >= template.getMaxServers()) {
            LOG.debug("Template '{}' reached max capacity ({}/{}), skipping",
                template.getName(), currentCount, template.getMaxServers());
            return 0;
        }

        return template.getMinServers() - currentCount;
    }

    private CompletableFuture<List<Server>> startServers(ServerTemplate template, int count) {
        LOG.debug("Starting {} servers for template '{}'", count, template.getName());

        List<CompletableFuture<Server>> startFutures = IntStream.range(0, count)
            .mapToObj(index -> startServer(template, index + 1, count))
            .toList();

        return CompletableFuture.allOf(startFutures.toArray(CompletableFuture[]::new))
            .thenApply(_ -> startFutures.stream()
                .map(CompletableFuture::join)
                .toList()
            );
    }

    private CompletableFuture<Server> startServer(ServerTemplate template, int position, int totalCount) {
        List<Node> readyNodes = listNodesUseCase.listByStatus(NodeStatus.READY);
        List<Node> suitableNodes = nodeScheduler.selectSuitableNodes(readyNodes, template);

        if (suitableNodes.isEmpty()) {
            LOG.warn("No suitable nodes found for template '{}'", template.getName());
            return CompletableFuture.failedFuture(new NoSuitableNodeException(template));
        }

        CompletableFuture<Server> future = new CompletableFuture<>();
        attemptDeployment(template, position, totalCount, suitableNodes, 0, future);
        return future;
    }

    private void attemptDeployment(
        ServerTemplate template,
        int position,
        int totalCount,
        List<Node> nodes,
        int nodeIndex,
        CompletableFuture<Server> future
    ) {
        if (nodeIndex >= nodes.size()) {
            future.completeExceptionally(new NoSuitableNodeException(template));
            return;
        }

        Node node = nodes.get(nodeIndex);
        LOG.debug("Attempting deployment {}/{} on node '{}'", position, totalCount, node.getName());

        if (!nodeScheduler.reserveResources(node, template.getRequirements())) {
            LOG.debug("Node '{}' cannot accommodate resources, trying next", node.getName());
            attemptDeployment(template, position, totalCount, nodes, nodeIndex + 1, future);
            return;
        }

        Server server = serverCreator.createServer(node.getName(), template);

        deployServerUseCase.deploy(server)
            .thenAccept(_ -> {
                LOG.info("Successfully deployed server {} ({}/{}) for template '{}' on node '{}'",
                    server.getName(), position, totalCount, template.getName(), node.getName());
                future.complete(server);
            })
            .exceptionally(t -> {
                LOG.error("Failed to deploy server {}/{} for template '{}' on node '{}'",
                    position, totalCount, template.getName(), node.getName(), t);

                nodeScheduler.releaseResources(node, template.getRequirements());
                serverCreator.removeServer(server);

                attemptDeployment(template, position, totalCount, nodes, nodeIndex + 1, future);
                return null;
            });
    }
}
