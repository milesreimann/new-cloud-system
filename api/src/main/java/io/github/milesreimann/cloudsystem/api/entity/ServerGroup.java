package io.github.milesreimann.cloudsystem.api.entity;

import io.github.milesreimann.cloudsystem.api.model.ServerGroupType;

import java.util.Set;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public interface ServerGroup {
     Long getId();

     String getAbbreviation();

     String getName();

     ServerGroupType getType();

     Set<String> getRequiredNodeLabels();

     Set<String> getPreferredNodeLabels();

     boolean isActive();
}