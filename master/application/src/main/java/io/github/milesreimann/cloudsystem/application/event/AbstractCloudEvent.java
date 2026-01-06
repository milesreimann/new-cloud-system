package io.github.milesreimann.cloudsystem.application.event;

import io.github.milesreimann.cloudsystem.api.event.CloudEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public abstract class AbstractCloudEvent implements CloudEvent {
    private final UUID uniqueId = UUID.randomUUID();
    private final Instant timestamp = Instant.now();

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
