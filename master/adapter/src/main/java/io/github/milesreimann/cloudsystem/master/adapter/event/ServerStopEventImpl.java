package io.github.milesreimann.cloudsystem.master.adapter.event;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.event.ServerStopEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerStopEventImpl extends CloudEventImpl implements ServerStopEvent {
    private final Server server;
}