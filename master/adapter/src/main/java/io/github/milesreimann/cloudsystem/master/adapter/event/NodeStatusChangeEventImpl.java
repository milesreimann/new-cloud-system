package io.github.milesreimann.cloudsystem.master.adapter.event;

import io.github.milesreimann.cloudsystem.api.entity.Node;
import io.github.milesreimann.cloudsystem.api.event.NodeStatusChangeEvent;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Miles R.
 * @since 27.12.2025
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NodeStatusChangeEventImpl extends CloudEventImpl implements NodeStatusChangeEvent {
    private final Node node;
    private final NodeStatus oldStatus;
    private final NodeStatus newStatus;
}
