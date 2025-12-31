package io.github.milesreimann.cloudsystem.master.adapter.event;

import io.github.milesreimann.cloudsystem.api.event.CloudEvent;
import lombok.Data;

import java.time.Instant;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
@Data
public class CloudEventImpl implements CloudEvent {
    private final Instant timestamp = Instant.now();
}
