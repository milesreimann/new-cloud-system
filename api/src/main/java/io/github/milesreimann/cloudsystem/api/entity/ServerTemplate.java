package io.github.milesreimann.cloudsystem.api.entity;

import io.github.milesreimann.cloudsystem.api.model.DeploymentType;
import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    Optional<Resources> getLimits();

    List<DeploymentMetadata> getDeploymentMetadata();

    Map<String, String> getEnvironmentVariables();

    boolean isActive();

    default Optional<String> getDeploymentMetadataValue(DeploymentType type, String key) {
        if (getDeploymentMetadata() == null) {
            return Optional.empty();
        }

        return getDeploymentMetadata().stream()
            .filter(metadata -> metadata.getDeploymentType() == type && metadata.getKey().equals(key))
            .findFirst()
            .map(DeploymentMetadata::getValue);
    }
}