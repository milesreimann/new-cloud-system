package io.github.milesreimann.cloudsystem.api.event;

import io.github.milesreimann.cloudsystem.api.entity.Node;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public interface NodeStatusChangeEvent extends CloudEvent {
    Node getNode();

    NodeStatus getOldStatus();

    NodeStatus getNewStatus();
}