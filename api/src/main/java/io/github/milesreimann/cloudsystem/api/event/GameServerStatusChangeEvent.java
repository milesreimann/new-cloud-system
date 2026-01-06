package io.github.milesreimann.cloudsystem.api.event;

import io.github.milesreimann.cloudsystem.api.entity.GameServer;
import io.github.milesreimann.cloudsystem.api.model.GameServerStatus;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public interface GameServerStatusChangeEvent extends CloudEvent {
    GameServer getGameServer();

    GameServerStatus getOldStatus();

    GameServerStatus getNewStatus();
}