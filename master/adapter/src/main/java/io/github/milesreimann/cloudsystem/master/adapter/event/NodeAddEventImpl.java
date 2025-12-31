package io.github.milesreimann.cloudsystem.master.adapter.event;

import io.github.milesreimann.cloudsystem.api.entity.Node;
import io.github.milesreimann.cloudsystem.api.event.NodeAddEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NodeAddEventImpl extends CloudEventImpl implements NodeAddEvent {
    private final Node node;
}
