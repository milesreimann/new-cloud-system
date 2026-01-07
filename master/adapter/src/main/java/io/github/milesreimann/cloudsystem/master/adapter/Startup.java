package io.github.milesreimann.cloudsystem.master.adapter;

import io.github.milesreimann.cloudsystem.api.event.EventBus;
import io.github.milesreimann.cloudsystem.api.event.GameServerStatusChangeEvent;
import io.github.milesreimann.cloudsystem.api.event.ServerStopEvent;
import io.github.milesreimann.cloudsystem.api.event.ServerTemplateUpdateEvent;
import io.github.milesreimann.cloudsystem.application.listener.GameServerStatusChangeListener;
import io.github.milesreimann.cloudsystem.application.listener.ServerStopListener;
import io.github.milesreimann.cloudsystem.application.listener.ServerTemplateUpdateListener;
import io.github.milesreimann.cloudsystem.application.service.NodeInitializationService;
import io.github.milesreimann.cloudsystem.application.service.ServerSchedulerService;
import io.github.milesreimann.cloudsystem.application.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
@Component
@RequiredArgsConstructor
public class Startup implements ApplicationRunner {
    private final EventBus eventBus;
    private final ServerService serverService;
    private final ServerSchedulerService serverSchedulingService;
    private final NodeInitializationService nodeInitializationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        eventBus.register(GameServerStatusChangeEvent.class, new GameServerStatusChangeListener(serverSchedulingService));
        eventBus.register(ServerStopEvent.class, new ServerStopListener(serverService, serverSchedulingService));
        eventBus.register(ServerTemplateUpdateEvent.class, new ServerTemplateUpdateListener(serverSchedulingService));

        nodeInitializationService.initialize();
    }
}
