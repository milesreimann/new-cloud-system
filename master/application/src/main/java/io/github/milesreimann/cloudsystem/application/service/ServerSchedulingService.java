package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.entity.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.application.scheduling.filter.NodeFilterStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.scoring.NodeScoringStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public class ServerSchedulingService {
    private final NodeService nodeService;
    private final List<NodeFilterStrategy> filterStrategies;
    private final List<NodeScoringStrategy> scoringStrategies;

    public ServerSchedulingService(
        NodeService nodeService,
        List<NodeFilterStrategy> filterStrategies,
        List<NodeScoringStrategy> scoringStrategies
    ) {
        this.nodeService = nodeService;
        this.filterStrategies = filterStrategies;
        this.scoringStrategies = scoringStrategies;
    }

    public Optional<Node> selectNodeForTemplate(ServerTemplate serverTemplate) {
        if (!serverTemplate.isActive() || serverTemplate.getMinServers() == 0) {
            return Optional.empty();
        }

        List<Node> readyNodes = nodeService.listNodesByStatus(NodeStatus.READY);
        if (readyNodes.isEmpty()) {
            return Optional.empty();
        }

        List<Node> filteredNodes = applyFilters(readyNodes, serverTemplate);
        if (filteredNodes.isEmpty()) {
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
