package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.api.model.Label;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public class NodeService {
    private final NodeCache nodeCache;

    public NodeService(NodeCache nodeCache) {
        this.nodeCache = nodeCache;
    }

    public Optional<Node> getNodeById(String nodeId) {
        return nodeCache.get(nodeId);
    }

    public List<Node> listNodes() {
        return new ArrayList<>(nodeCache.values());
    }

    public List<Node> listNodesByLabel(Label label) {
        return listNodes().stream()
            .filter(node -> node.hasLabel(label))
            .toList();
    }

    public List<Node> listNodesByStatus(NodeStatus status) {
        return listNodes().stream()
            .filter(node -> node.getStatus() == status)
            .toList();
    }
}