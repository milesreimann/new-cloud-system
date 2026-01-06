package io.github.milesreimann.cloudsystem.api.event;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public interface CloudEvent {
    UUID getUniqueId();

    Instant getTimestamp();
}