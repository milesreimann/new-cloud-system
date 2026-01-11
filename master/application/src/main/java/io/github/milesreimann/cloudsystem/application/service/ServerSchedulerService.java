package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.application.exception.NoSuitableNodeException;
import io.github.milesreimann.cloudsystem.application.exception.ServerTemplateNotFoundException;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.NodeFilterStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.scoring.NodeScoringStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public class ServerSchedulerService {
    private static final Logger LOG = LoggerFactory.getLogger(ServerSchedulerService.class);

    private final NodeService nodeService;
    private final NodeReservationService nodeReservationService;
    private final ServerTemplateService serverTemplateService;
    private final ServerService serverService;
    private final ServerDeploymentPort deploymentPort;
    private final List<NodeFilterStrategy> filterStrategies;
    private final List<NodeScoringStrategy> scoringStrategies;

    public ServerSchedulerService(
        NodeService nodeService,
        NodeReservationService nodeReservationService,
        ServerTemplateService serverTemplateService,
        ServerService serverService,
        ServerDeploymentPort deploymentPort,
        List<NodeFilterStrategy> filterStrategies,
        List<NodeScoringStrategy> scoringStrategies
    ) {
        this.nodeService = nodeService;
        this.nodeReservationService = nodeReservationService;
        this.serverTemplateService = serverTemplateService;
        this.serverService = serverService;
        this.deploymentPort = deploymentPort;
        this.filterStrategies = filterStrategies;
        this.scoringStrategies = scoringStrategies;
    }

    public CompletableFuture<List<Server>> scheduleActiveServerTemplates() {
        LOG.info("Starting scheduling all active templates");

        List<ServerTemplate> activeTemplates = serverTemplateService.listServerTemplates(true);
        if (activeTemplates.isEmpty()) {
            LOG.info("No active templates found, nothing to schedule");
            return CompletableFuture.completedFuture(List.of());
        }

        LOG.debug("Found {} active templates to schedule", activeTemplates.size());

        List<CompletableFuture<List<Server>>> futures = activeTemplates.stream()
            .map(serverTemplate -> scheduleServerTemplate(serverTemplate.getId())
                .exceptionally(t -> {
                    LOG.error(t.getMessage());
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

                    LOG.info("Scheduling completed: {} servers started across {} templates", servers.size(), activeTemplates.size());
                    return servers;
                }
            );
    }

    public CompletableFuture<List<Server>> scheduleServerTemplate(long templateId) {
        return serverTemplateService.getServerTemplateById(templateId)
            .map(serverTemplate -> {
                LOG.debug("Scheduling template: '{}'", serverTemplate.getName());

                if (serverTemplate.getMinServers() <= 0) {
                    LOG.trace("Template '{}' has no positive minServers, skipping scheduling", serverTemplate.getName());
                    return CompletableFuture.completedFuture(List.<Server>of());
                }

                int currentCount = serverService.getServerCountForTemplate(serverTemplate.getId());
                LOG.debug("Template '{}' currently has {} running servers", serverTemplate.getName(), currentCount);

                if (currentCount >= serverTemplate.getMaxServers()) {
                    LOG.debug(
                        "Template '{}' reached max capacity ({}/{}), skipping",
                        serverTemplate.getName(), currentCount, serverTemplate.getMaxServers()
                    );
                    return CompletableFuture.completedFuture(List.<Server>of());
                }

                int serversToStart = serverTemplate.getMinServers() - currentCount;

                LOG.info("Scheduling {} servers for template '{}' (current: {}, min: {}, max: {})",
                    serversToStart,
                    serverTemplate.getName(),
                    currentCount,
                    serverTemplate.getMinServers(),
                    serverTemplate.getMaxServers()
                );

                return startServers(serverTemplate, serversToStart);
            })
            .orElseGet(() -> {
                LOG.warn("Template with id={} was not found", templateId);
                return CompletableFuture.failedFuture(new ServerTemplateNotFoundException(templateId));
            });
    }

    private CompletableFuture<List<Server>> startServers(ServerTemplate serverTemplate, int count) {
        if (count < 1) {
            LOG.trace("No servers to start because passed count is not positive");
            return CompletableFuture.completedFuture(List.of());
        }

        LOG.debug("Starting {} servers for template '{}'", count, serverTemplate.getName());

        List<CompletableFuture<Server>> startFutures = IntStream.range(0, count)
            .mapToObj(index -> startServer(serverTemplate, index + 1, count))
            .toList();

        return CompletableFuture.allOf(startFutures.toArray(CompletableFuture[]::new))
            .thenApply(_ -> startFutures.stream()
                .map(CompletableFuture::join)
                .toList()
            );
    }

    private CompletableFuture<Server> startServer(ServerTemplate serverTemplate, int position, int totalCount) {
        List<Node> possibleNodes = determinePossibleNodes(serverTemplate);
        if (possibleNodes.isEmpty()) {
            LOG.warn("No suitable nodes found for template '{}' (no nodes passed filter criteria)", serverTemplate.getName());
            return CompletableFuture.failedFuture(new NoSuitableNodeException(serverTemplate));
        }

        CompletableFuture<Server> future = new CompletableFuture<>();
        tryDeployServerOnNode(serverTemplate, position, totalCount, possibleNodes, 0, future);
        return future;
    }

    private void tryDeployServerOnNode(
        ServerTemplate serverTemplate,
        int position,
        int totalCount,
        List<Node> nodes,
        int nodeIndex,
        CompletableFuture<Server> future
    ) {
        if (nodeIndex >= nodes.size()) {
            future.completeExceptionally(new NoSuitableNodeException(serverTemplate));
            return;
        }

        Node node = nodes.get(nodeIndex);
        LOG.debug("Trying to deploy server {}/{} on node '{}'", position, totalCount, node.getName());

        if (!nodeReservationService.tryReserve(node, serverTemplate.getRequirements())) {
            tryDeployServerOnNode(serverTemplate, position, totalCount, nodes, nodeIndex + 1, future);
            return;
        }

        LOG.info("Node {} reserved: {}", node.getName(), nodeReservationService.getReserved(node));

        LOG.debug(
            "Reserved resources on node '{}' for server {}/{} of template '{}'",
            node.getName(), position, totalCount, serverTemplate.getName()
        );

        Server server = serverService.addServer(serverTemplate);

        deploymentPort.deployServer(node, server)
            .thenAccept(_ -> {
                LOG.info(
                    "Successfully deployed server {} ({}/{}) for template '{}': {} on node '{}'",
                    server.getName(), position, totalCount, serverTemplate.getName(), server.getUniqueId(), node.getName()
                );

                future.complete(server);
            })
            .exceptionally(t -> {
                LOG.error(
                    "Failed to deploy server {}/{} for template '{}' on node '{}'",
                    position, totalCount, serverTemplate.getName(), node.getName(), t
                );

                nodeReservationService.release(node, serverTemplate.getRequirements());
                serverService.removeServer(server);

                tryDeployServerOnNode(serverTemplate, position, totalCount, nodes, nodeIndex + 1, future);
                return null;
            });
    }

    private List<Node> determinePossibleNodes(ServerTemplate serverTemplate) {
        List<Node> readyNodes = nodeService.listNodesByStatus(NodeStatus.READY);
        if (readyNodes.isEmpty()) {
            LOG.warn("No nodes in READY status available for template '{}' ", serverTemplate.getName());
            return List.of();
        }

        List<Node> filteredNodes = filterNodes(readyNodes, serverTemplate);
        if (filteredNodes.isEmpty()) {
            LOG.warn("All {} nodes filtered out for template '{}' (no nodes passed filter criteria)", readyNodes.size(), serverTemplate.getName());
            return List.of();
        }

        LOG.trace("{} nodes passed filtering for template '{}'", filteredNodes.size(), serverTemplate.getName());
        return scoreNodes(filteredNodes, serverTemplate);
    }

    private List<Node> filterNodes(List<Node> nodes, ServerTemplate template) {
        return filterStrategies.stream()
            .sorted(Comparator.comparingInt(NodeFilterStrategy::getPriority).reversed())
            .reduce(
                nodes,
                (currentNodes, strategy) -> strategy.filter(currentNodes, template),
                (_, b) -> b
            );
    }

    private List<Node> scoreNodes(List<Node> nodes, ServerTemplate template) {
        return nodes.stream()
            .sorted(Comparator.comparingDouble(node -> -scoringStrategies.stream().mapToDouble(s -> s.score(node, template)).sum()))
            .toList();
    }
}