package io.github.milesreimann.cloudsystem.adapter.spring.event;

import io.github.milesreimann.cloudsystem.api.event.CloudEvent;
import io.github.milesreimann.cloudsystem.api.event.EventBus;
import io.github.milesreimann.cloudsystem.api.event.EventListener;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
public record SimpleEventRegistration<E extends CloudEvent>(
    Class<E> eventType,
    EventListener<E> listener
) implements EventRegistration {
    @Override
    public void register(EventBus eventBus) {
        eventBus.register(eventType, listener);
    }
}