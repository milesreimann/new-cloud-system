package io.github.milesreimann.cloudsystem.adapter.spring.event;

import io.github.milesreimann.cloudsystem.api.event.EventBus;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
public interface EventRegistration {
    void register(EventBus eventBus);
}