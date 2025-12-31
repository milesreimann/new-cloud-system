package io.github.milesreimann.cloudsystem.master.adapter.event;

import io.github.milesreimann.cloudsystem.api.entity.Node;
import io.github.milesreimann.cloudsystem.api.event.NodeRemoveEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NodeRemoveEventImpl extends CloudEventImpl implements NodeRemoveEvent {
    private final Node node;
}
