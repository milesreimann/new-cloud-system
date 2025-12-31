package io.github.milesreimann.cloudsystem.api.event;

import io.github.milesreimann.cloudsystem.api.entity.Node;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public interface NodeAddEvent extends CloudEvent {
    Node getNode();
}