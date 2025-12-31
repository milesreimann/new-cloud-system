package io.github.milesreimann.cloudsystem.api.event;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.model.ServerStatus;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public interface ServerStatusChangeEvent extends CloudEvent {
    Server getServer();

    ServerStatus getOldStatus();

    ServerStatus getNewStatus();
}