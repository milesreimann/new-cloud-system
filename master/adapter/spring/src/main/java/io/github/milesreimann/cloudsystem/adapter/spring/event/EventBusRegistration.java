package io.github.milesreimann.cloudsystem.adapter.spring.event;

import io.github.milesreimann.cloudsystem.api.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Component
@RequiredArgsConstructor
public class EventBusRegistration implements ApplicationRunner {
    private final EventBus eventBus;
    private final List<EventRegistration> eventRegistrations;

    @Override
    public void run(ApplicationArguments args) {
        eventRegistrations.forEach(eventRegistration -> eventRegistration.register(eventBus));
    }
}