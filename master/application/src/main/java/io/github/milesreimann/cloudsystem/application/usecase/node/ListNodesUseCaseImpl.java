package io.github.milesreimann.cloudsystem.application.usecase.node;

import io.github.milesreimann.cloudsystem.api.model.Label;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.in.node.ListNodesUseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class ListNodesUseCaseImpl implements ListNodesUseCase {
    private final NodeCache nodeCache;

    public ListNodesUseCaseImpl(NodeCache nodeCache) {
        this.nodeCache = nodeCache;
    }

    @Override
    public List<Node> listAll() {
        return new ArrayList<>(nodeCache.values());
    }

    @Override
    public List<Node> listByLabel(Label label) {
        return listAll().stream()
            .filter(node -> node.hasLabel(label))
            .toList();
    }

    @Override
    public List<Node> listByStatus(NodeStatus status) {
        return listAll().stream()
            .filter(node -> node.getStatus() == status)
            .toList();
    }
}