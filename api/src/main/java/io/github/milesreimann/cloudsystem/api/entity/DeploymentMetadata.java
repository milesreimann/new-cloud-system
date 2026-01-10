package io.github.milesreimann.cloudsystem.api.entity;

import io.github.milesreimann.cloudsystem.api.model.DeploymentType;

/**
 * @author Miles R.
 * @since 09.01.2026
 */
public interface DeploymentMetadata {
    Long getId();

    ServerTemplate getServerTemplate();

    DeploymentType getDeploymentType();

    String getKey();

    String getValue();
}