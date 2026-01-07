package io.github.milesreimann.cloudsystem.application.listener;

import io.github.milesreimann.cloudsystem.api.event.EventListener;
import io.github.milesreimann.cloudsystem.api.event.ServerTemplateUpdateEvent;
import io.github.milesreimann.cloudsystem.application.service.ServerSchedulerService;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class ServerTemplateUpdateListener implements EventListener<ServerTemplateUpdateEvent> {
    private final ServerSchedulerService serverSchedulingService;

    public ServerTemplateUpdateListener(ServerSchedulerService serverSchedulingService) {
        this.serverSchedulingService = serverSchedulingService;
    }

    @Override
    public void handle(ServerTemplateUpdateEvent event) {
        serverSchedulingService.scheduleServerTemplate(event.getServerTemplate().getId());
    }
}