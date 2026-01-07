package io.github.milesreimann.cloudsystem.application.listener;

import io.github.milesreimann.cloudsystem.api.event.EventListener;
import io.github.milesreimann.cloudsystem.api.event.GameServerStatusChangeEvent;
import io.github.milesreimann.cloudsystem.api.model.GameServerStatus;
import io.github.milesreimann.cloudsystem.application.service.ServerSchedulerService;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class GameServerStatusChangeListener implements EventListener<GameServerStatusChangeEvent> {
    private final ServerSchedulerService serverSchedulingService;

    public GameServerStatusChangeListener(ServerSchedulerService serverSchedulingService) {
        this.serverSchedulingService = serverSchedulingService;
    }

    @Override
    public void handle(GameServerStatusChangeEvent event) {
        if (event.getOldStatus() == GameServerStatus.LOBBY && event.getNewStatus() == GameServerStatus.IN_GAME) {
            serverSchedulingService.scheduleServerTemplate(event.getGameServer().getTemplateId());
        }
    }
}