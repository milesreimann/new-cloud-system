package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.entity.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.NodeFilterStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.scoring.NodeScoringStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public class ServerSchedulingService {
    private final Logger log = LoggerFactory.getLogger(ServerSchedulingService.class);

    private final NodeService nodeService;
    private final ServerTemplateService serverTemplateService;
    private final ServerService serverService;
    private final List<NodeFilterStrategy> filterStrategies;
    private final List<NodeScoringStrategy> scoringStrategies;

    public ServerSchedulingService(
        NodeService nodeService,
        ServerTemplateService serverTemplateService,
        ServerService serverService,
        List<NodeFilterStrategy> filterStrategies,
        List<NodeScoringStrategy> scoringStrategies
    ) {
        this.nodeService = nodeService;
        this.serverService = serverService;
        this.serverTemplateService = serverTemplateService;
        this.filterStrategies = filterStrategies;
        this.scoringStrategies = scoringStrategies;
    }

    // TODO: event driven scheduling

    public void schedule() {
        log.debug("Scheduling servers...");

        for (ServerTemplate serverTemplate : serverTemplateService.listServerTemplates(true)) {
            log.debug("Scheduling server template: {} (id={})", serverTemplate.getName(), serverTemplate.getId());

            if (serverTemplate.getMinServers() == 0) {
                log.trace("Template '{}' minServers=0, skipping", serverTemplate.getName());
                continue;
            }

            long serverCount = serverService.getServerCountForTemplate(serverTemplate.getId());

            if (serverCount >= serverTemplate.getMaxServers()) {
                log.trace("Max servers reached for template '{}', skipping", serverTemplate.getName());
                continue;
            }

            long toStart = serverTemplate.getMinServers() - serverCount;
            log.debug("{} servers to start for template '{}'", toStart, serverTemplate.getName());

            for (int i = 0; i < toStart; i++) {
                final int finalI = i;

                selectNodeForTemplate(serverTemplate).ifPresentOrElse(
                    node -> log.info("Selected node '{}' for server #{} of template '{}'", node.getName(), finalI, serverTemplate.getName()),
                    () -> log.warn("No suitable node available for template '{}'", serverTemplate.getName())
                );

                // deploy or smth
            }
        }
    }

    private Optional<Node> selectNodeForTemplate(ServerTemplate serverTemplate) {
        List<Node> readyNodes = nodeService.listNodesByStatus(NodeStatus.READY);
        if (readyNodes.isEmpty()) {
            log.warn("No ready nodes available for template '{}'", serverTemplate.getName());
            return Optional.empty();
        }

        List<Node> filteredNodes = applyFilters(readyNodes, serverTemplate);
        if (filteredNodes.isEmpty()) {
            log.warn("No nodes passed filters for template '{}'", serverTemplate.getName());
            return Optional.empty();
        }

        return selectBestNode(filteredNodes, serverTemplate);
    }

    private List<Node> applyFilters(List<Node> nodes, ServerTemplate template) {
        return filterStrategies.stream()
            .sorted(Comparator.comparingInt(NodeFilterStrategy::getPriority).reversed())
            .reduce(
                nodes,
                (currentNodes, strategy) -> strategy.filter(currentNodes, template),
                (_, b) -> b
            );
    }

    private Optional<Node> selectBestNode(List<Node> nodes, ServerTemplate template) {
        return nodes.stream()
            .max(Comparator.comparingDouble(node -> scoringStrategies.stream()
                .mapToDouble(scorer -> scorer.score(node, template))
                .sum()
            ));
    }
}
