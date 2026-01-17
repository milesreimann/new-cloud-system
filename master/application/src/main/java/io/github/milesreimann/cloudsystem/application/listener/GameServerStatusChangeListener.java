package io.github.milesreimann.cloudsystem.application.listener;

import io.github.milesreimann.cloudsystem.api.event.EventListener;
import io.github.milesreimann.cloudsystem.api.event.GameServerStatusChangeEvent;
import io.github.milesreimann.cloudsystem.api.model.GameServerStatus;
import io.github.milesreimann.cloudsystem.application.port.in.server.ScheduleServersUseCase;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class GameServerStatusChangeListener implements EventListener<GameServerStatusChangeEvent> {
    private final ScheduleServersUseCase scheduleServersUseCase;

    public GameServerStatusChangeListener(ScheduleServersUseCase scheduleServersUseCase) {
        this.scheduleServersUseCase = scheduleServersUseCase;
    }

    @Override
    public void handle(GameServerStatusChangeEvent event) {
        if (event.getOldStatus() == GameServerStatus.LOBBY && event.getNewStatus() == GameServerStatus.IN_GAME) {
            scheduleServersUseCase.scheduleTemplate(event.getGameServer().getTemplate().getId());
        }
    }
}