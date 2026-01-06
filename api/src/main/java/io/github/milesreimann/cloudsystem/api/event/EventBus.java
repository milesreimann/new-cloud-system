package io.github.milesreimann.cloudsystem.api.event;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public interface EventBus {
    <E extends CloudEvent> void publish(E event);

    <E extends CloudEvent> void register(Class<E> eventType, EventListener<E> listener);
}