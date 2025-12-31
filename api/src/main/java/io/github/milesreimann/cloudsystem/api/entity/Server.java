package io.github.milesreimann.cloudsystem.api.entity;

import io.github.milesreimann.cloudsystem.api.model.ServerStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Miles R.
 * @since 29.12.2025
 */
public interface Server {
    UUID getUniqueId();

    long getId();

    String getNodeId();

    Long getTemplateId();

    ServerStatus getStatus();

    Instant getStartedAt();
}