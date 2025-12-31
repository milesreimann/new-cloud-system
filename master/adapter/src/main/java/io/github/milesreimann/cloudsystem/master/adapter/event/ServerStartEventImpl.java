package io.github.milesreimann.cloudsystem.master.adapter.event;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.event.ServerStartEvent;
import io.github.milesreimann.cloudsystem.api.strategy.NodeFilterStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerStartEventImpl extends CloudEventImpl implements ServerStartEvent {
    private final Server server;
    private final Class<? extends NodeFilterStrategy> nodeSelectionStrategy;
}