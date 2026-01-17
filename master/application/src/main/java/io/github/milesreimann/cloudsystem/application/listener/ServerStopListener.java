package io.github.milesreimann.cloudsystem.application.listener;

import io.github.milesreimann.cloudsystem.api.entity.GameServer;
import io.github.milesreimann.cloudsystem.api.event.EventListener;
import io.github.milesreimann.cloudsystem.api.event.ServerStopEvent;
import io.github.milesreimann.cloudsystem.application.port.in.server.ScheduleServersUseCase;
import io.github.milesreimann.cloudsystem.application.usecase.server.ServerCreator;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class ServerStopListener implements EventListener<ServerStopEvent> {
    private final ServerCreator serverCreator;
    private final ScheduleServersUseCase scheduleServersUseCase;

    public ServerStopListener(ServerCreator serverCreator, ScheduleServersUseCase scheduleServersUseCase) {
        this.serverCreator = serverCreator;
        this.scheduleServersUseCase = scheduleServersUseCase;
    }

    @Override
    public void handle(ServerStopEvent event) {
        serverCreator.removeServer(event.getServer());

        if (!(event.getServer() instanceof GameServer)) {
            scheduleServersUseCase.scheduleTemplate(event.getServer().getTemplate().getId());
        }
    }
}