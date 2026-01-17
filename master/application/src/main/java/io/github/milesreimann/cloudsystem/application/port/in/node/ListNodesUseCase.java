package io.github.milesreimann.cloudsystem.application.port.in.node;

import io.github.milesreimann.cloudsystem.api.model.Label;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.runtime.Node;

import java.util.List;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public interface ListNodesUseCase {
    List<Node> listAll();

    List<Node> listByStatus(NodeStatus status);

    List<Node> listByLabel(Label label);
}