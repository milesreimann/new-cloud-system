package io.github.milesreimann.cloudsystem.adapter.spring.config;

import io.github.milesreimann.cloudsystem.adapter.spring.event.EventRegistration;
import io.github.milesreimann.cloudsystem.adapter.spring.event.SimpleEventRegistration;
import io.github.milesreimann.cloudsystem.api.event.EventBus;
import io.github.milesreimann.cloudsystem.api.event.GameServerStatusChangeEvent;
import io.github.milesreimann.cloudsystem.api.event.ServerStopEvent;
import io.github.milesreimann.cloudsystem.api.event.ServerTemplateUpdateEvent;
import io.github.milesreimann.cloudsystem.application.event.SimpleEventBus;
import io.github.milesreimann.cloudsystem.application.listener.GameServerStatusChangeListener;
import io.github.milesreimann.cloudsystem.application.listener.ServerStopListener;
import io.github.milesreimann.cloudsystem.application.listener.ServerTemplateUpdateListener;
import io.github.milesreimann.cloudsystem.application.service.ServerSchedulerService;
import io.github.milesreimann.cloudsystem.application.service.ServerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Configuration
public class EventConfig {
    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public EventRegistration gameServerStatusChangeListener(
        EventBus eventBus,
        ServerSchedulerService serverSchedulerService
    ) {
        return new SimpleEventRegistration<>(
            GameServerStatusChangeEvent.class,
            new GameServerStatusChangeListener(serverSchedulerService)
        );
    }

    @Bean
    public EventRegistration serverStopListener(
        EventBus eventBus,
        ServerService serverService,
        ServerSchedulerService serverSchedulerService
    ) {
        return new SimpleEventRegistration<>(
            ServerStopEvent.class,
            new ServerStopListener(serverService, serverSchedulerService)
        );
    }

    @Bean
    public EventRegistration serverTemplateUpdateListener(
        EventBus eventBus,
        ServerSchedulerService serverSchedulerService
    ) {
        return new SimpleEventRegistration<>(
            ServerTemplateUpdateEvent.class,
            new ServerTemplateUpdateListener(serverSchedulerService)
        );
    }
}