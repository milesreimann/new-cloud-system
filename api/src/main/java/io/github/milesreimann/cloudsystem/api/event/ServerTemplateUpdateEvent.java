package io.github.milesreimann.cloudsystem.api.event;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public interface ServerTemplateUpdateEvent extends CloudEvent {
    ServerTemplate getServerTemplate();
}