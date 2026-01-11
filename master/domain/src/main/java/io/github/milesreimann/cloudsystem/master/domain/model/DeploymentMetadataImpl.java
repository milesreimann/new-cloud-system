package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.entity.DeploymentMetadata;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.DeploymentType;

import java.util.Objects;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
public class DeploymentMetadataImpl implements DeploymentMetadata {
    private final Long id;
    private final DeploymentType deploymentType;
    private final String key;
    private final String value;

    public DeploymentMetadataImpl(
        Long id,
        DeploymentType deploymentType,
        String key,
        String value
    ) {
        this.id = id;
        this.deploymentType = deploymentType;
        this.key = key;
        this.value = value;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeploymentMetadataImpl that = (DeploymentMetadataImpl) o;
        return Objects.equals(id, that.id) && deploymentType == that.deploymentType && Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deploymentType, key, value);
    }

    @Override
    public String toString() {
        return "DeploymentMetadataImpl{" +
            "id=" + id +
            ", deploymentType=" + deploymentType +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
