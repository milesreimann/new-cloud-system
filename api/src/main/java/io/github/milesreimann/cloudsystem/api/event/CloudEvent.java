package io.github.milesreimann.cloudsystem.api.event;

import java.time.Instant;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public interface CloudEvent {
    Instant getTimestamp();
}