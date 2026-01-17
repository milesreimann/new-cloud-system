package io.github.milesreimann.cloudsystem.api.runtime;

import io.github.milesreimann.cloudsystem.api.model.MasterStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public interface Master {
    // Future

    UUID getUniqueId();

    String getName();

    Instant getStartedAt();

    MasterStatus getStatus();

    MasterLeadership getLeadership();
}