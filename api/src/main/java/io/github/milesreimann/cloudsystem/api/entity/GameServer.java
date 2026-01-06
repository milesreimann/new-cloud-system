package io.github.milesreimann.cloudsystem.api.entity;

import io.github.milesreimann.cloudsystem.api.model.GameServerStatus;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public interface GameServer extends Server {
    GameServerStatus getGameServerStatus();

    void setGameServerStatus(GameServerStatus status);
}
