package io.github.milesreimann.cloudsystem.application.port.in.node;

import io.github.milesreimann.cloudsystem.api.runtime.Node;

import java.util.Optional;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public interface GetNodeUseCase {
    Optional<Node> getById(String nodeId);
}