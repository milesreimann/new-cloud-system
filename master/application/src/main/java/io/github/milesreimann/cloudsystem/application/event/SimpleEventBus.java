package io.github.milesreimann.cloudsystem.application.event;

import io.github.milesreimann.cloudsystem.api.event.CloudEvent;
import io.github.milesreimann.cloudsystem.api.event.EventBus;
import io.github.milesreimann.cloudsystem.api.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class SimpleEventBus implements EventBus {
    private final Map<Class<?>, List<EventListener<?>>> listenersByEventType = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <E extends CloudEvent> void publish(E event) {
        Class<?> type = event.getClass();

        List<EventListener<?>> listeners = listenersByEventType.get(type);
        if (listeners == null || listeners.isEmpty()) {
            return;
        }

        listeners.forEach(listener -> ((EventListener<E>) listener).handle(event));
    }

    @Override
    public <E extends CloudEvent> void register(Class<E> eventType, EventListener<E> listener) {
        listenersByEventType
            .computeIfAbsent(eventType, _ -> new ArrayList<>())
            .add(listener);
    }
}