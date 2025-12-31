package io.github.milesreimann.cloudsystem.api.event;

import io.github.milesreimann.cloudsystem.api.entity.Server;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public interface ServerStopEvent extends CloudEvent {
    Server getServer();
}