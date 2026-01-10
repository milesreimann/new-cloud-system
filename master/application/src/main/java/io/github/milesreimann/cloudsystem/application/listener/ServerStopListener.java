package io.github.milesreimann.cloudsystem.application.listener;

import io.github.milesreimann.cloudsystem.api.entity.GameServer;
import io.github.milesreimann.cloudsystem.api.event.EventListener;
import io.github.milesreimann.cloudsystem.api.event.ServerStopEvent;
import io.github.milesreimann.cloudsystem.application.service.ServerSchedulerService;
import io.github.milesreimann.cloudsystem.application.service.ServerService;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class ServerStopListener implements EventListener<ServerStopEvent> {
    private final ServerService serverService;
    private final ServerSchedulerService serverSchedulingService;

    public ServerStopListener(ServerService serverService, ServerSchedulerService serverSchedulingService) {
        this.serverService = serverService;
        this.serverSchedulingService = serverSchedulingService;
    }

    @Override
    public void handle(ServerStopEvent event) {
        serverService.removeServer(event.getServer());

        if (!(event.getServer() instanceof GameServer)) {
            serverSchedulingService.scheduleServerTemplate(event.getServer().getTemplate().getId());
        }
    }
}