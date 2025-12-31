package io.github.milesreimann.cloudsystem.api.entity;

import io.github.milesreimann.cloudsystem.api.model.Resources;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
public interface ServerTemplate {
    Long getId();

    String getAbbreviation();

    String getName();

    ServerGroup getGroup();

    int getMinServers();

    Integer getMaxServers();

    Resources getRequirements();

    boolean isActive();
}