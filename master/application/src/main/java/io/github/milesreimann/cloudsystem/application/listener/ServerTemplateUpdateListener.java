package io.github.milesreimann.cloudsystem.application.listener;

import io.github.milesreimann.cloudsystem.api.event.EventListener;
import io.github.milesreimann.cloudsystem.api.event.ServerTemplateUpdateEvent;
import io.github.milesreimann.cloudsystem.application.port.in.server.ScheduleServersUseCase;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class ServerTemplateUpdateListener implements EventListener<ServerTemplateUpdateEvent> {
    private final ScheduleServersUseCase scheduleServersUseCase;

    public ServerTemplateUpdateListener(ScheduleServersUseCase scheduleServersUseCase) {
        this.scheduleServersUseCase = scheduleServersUseCase;
    }

    @Override
    public void handle(ServerTemplateUpdateEvent event) {
        scheduleServersUseCase.scheduleTemplate(event.getServerTemplate().getId());
    }
}