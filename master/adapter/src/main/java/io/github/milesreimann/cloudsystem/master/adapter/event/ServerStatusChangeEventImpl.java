package io.github.milesreimann.cloudsystem.master.adapter.event;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.event.ServerStatusChangeEvent;
import io.github.milesreimann.cloudsystem.api.model.ServerStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Miles R.
 * @since 27.12.2025
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerStatusChangeEventImpl extends CloudEventImpl implements ServerStatusChangeEvent {
    private final Server server;
    private final ServerStatus oldStatus;
    private final ServerStatus newStatus;
}