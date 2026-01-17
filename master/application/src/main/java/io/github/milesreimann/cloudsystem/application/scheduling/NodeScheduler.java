package io.github.milesreimann.cloudsystem.application.scheduling;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter.NodeFilterStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.scoring.NodeScoringStrategy;
import io.github.milesreimann.cloudsystem.application.usecase.node.NodeResourceReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class NodeScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(NodeScheduler.class);

    private final List<NodeFilterStrategy> filterStrategies;
    private final List<NodeScoringStrategy> scoringStrategies;
    private final NodeResourceReservationService nodeResourceReservationService;

    public NodeScheduler(
        List<NodeFilterStrategy> filterStrategies,
        List<NodeScoringStrategy> scoringStrategies,
        NodeResourceReservationService nodeResourceReservationService
    ) {
        this.filterStrategies = filterStrategies;
        this.scoringStrategies = scoringStrategies;
        this.nodeResourceReservationService = nodeResourceReservationService;
    }

    public List<Node> selectSuitableNodes(List<Node> candidates, ServerTemplate template) {
        if (candidates.isEmpty()) {
            LOG.warn("No candidate nodes available");
            return List.of();
        }

        List<Node> filtered = applyFilters(candidates, template);
        if (filtered.isEmpty()) {
            LOG.warn("All {} nodes filtered out for template '{}'", candidates.size(), template.getName());
            return List.of();
        }

        LOG.trace("{} nodes passed filtering for template '{}'", filtered.size(), template.getName());

        return applyScoring(filtered, template);
    }

    public boolean reserveResources(Node node, Resources resources) {
        return nodeResourceReservationService.tryReserve(node, resources);
    }

    public void releaseResources(Node node, Resources resources) {
        nodeResourceReservationService.release(node, resources);
    }

    private List<Node> applyFilters(List<Node> nodes, ServerTemplate template) {
        return filterStrategies.stream()
            .sorted(Comparator.comparingInt(NodeFilterStrategy::getPriority).reversed())
            .reduce(
                nodes,
                (current, strategy) -> strategy.filter(current, template),
                (_, b) -> b
            );
    }

    private List<Node> applyScoring(List<Node> nodes, ServerTemplate template) {
        return nodes.stream()
            .sorted(Comparator.comparingDouble(node ->
                -scoringStrategies.stream()
                    .mapToDouble(s -> s.score(node, template))
                    .sum()
            ))
            .toList();
    }
}
