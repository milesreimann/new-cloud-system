package io.github.milesreimann.cloudsystem.application.usecase.node;

import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.in.node.GetNodeUseCase;

import java.util.Optional;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class GetNodeUseCaseImpl implements GetNodeUseCase {
    private final NodeCache nodeCache;

    public GetNodeUseCaseImpl(NodeCache nodeCache) {
        this.nodeCache = nodeCache;
    }

    @Override
    public Optional<Node> getById(String nodeId) {
        return nodeCache.get(nodeId);
    }
}