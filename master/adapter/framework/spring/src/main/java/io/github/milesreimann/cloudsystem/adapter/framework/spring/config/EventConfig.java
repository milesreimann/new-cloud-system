package io.github.milesreimann.cloudsystem.adapter.framework.spring.config;

import io.github.milesreimann.cloudsystem.adapter.framework.spring.event.EventRegistration;
import io.github.milesreimann.cloudsystem.adapter.framework.spring.event.SimpleEventRegistration;
import io.github.milesreimann.cloudsystem.api.event.EventBus;
import io.github.milesreimann.cloudsystem.api.event.GameServerStatusChangeEvent;
import io.github.milesreimann.cloudsystem.api.event.ServerStopEvent;
import io.github.milesreimann.cloudsystem.api.event.ServerTemplateUpdateEvent;
import io.github.milesreimann.cloudsystem.application.event.SimpleEventBus;
import io.github.milesreimann.cloudsystem.application.listener.GameServerStatusChangeListener;
import io.github.milesreimann.cloudsystem.application.listener.ServerStopListener;
import io.github.milesreimann.cloudsystem.application.listener.ServerTemplateUpdateListener;
import io.github.milesreimann.cloudsystem.application.port.in.server.ScheduleServersUseCase;
import io.github.milesreimann.cloudsystem.application.usecase.server.ServerCreator;
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
    public EventRegistration gameServerStatusChangeListener(ScheduleServersUseCase scheduleServersUseCase) {
        return new SimpleEventRegistration<>(
            GameServerStatusChangeEvent.class,
            new GameServerStatusChangeListener(scheduleServersUseCase)
        );
    }

    @Bean
    public EventRegistration serverStopListener(
        ServerCreator serverCreator,
        ScheduleServersUseCase scheduleServersUseCase
    ) {
        return new SimpleEventRegistration<>(
            ServerStopEvent.class,
            new ServerStopListener(serverCreator, scheduleServersUseCase)
        );
    }

    @Bean
    public EventRegistration serverTemplateUpdateListener(ScheduleServersUseCase scheduleServersUseCase) {
        return new SimpleEventRegistration<>(
            ServerTemplateUpdateEvent.class,
            new ServerTemplateUpdateListener(scheduleServersUseCase)
        );
    }
}