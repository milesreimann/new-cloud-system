package io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.runtime.Node;

import java.util.List;

/**
 * @author Miles R.
 * @since 08.01.2026
 */
public class NodeStatusStrategy implements NodeFilterStrategy {
    @Override
    public List<Node> filter(List<Node> candidates, ServerTemplate serverTemplate) {
        return candidates.stream()
            .filter(node -> node.getStatus() == NodeStatus.READY)
            .toList();
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
