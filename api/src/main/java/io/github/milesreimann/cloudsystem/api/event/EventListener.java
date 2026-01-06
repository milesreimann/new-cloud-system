package io.github.milesreimann.cloudsystem.api.event;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public interface EventListener<E extends CloudEvent> {
    void handle(E event);
}
