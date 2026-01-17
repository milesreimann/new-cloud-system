package io.github.milesreimann.cloudsystem.adapter.spring.mapper;

import io.github.milesreimann.cloudsystem.adapter.spring.persistence.entity.JpaDeploymentMetadata;
import io.github.milesreimann.cloudsystem.api.entity.DeploymentMetadata;
import io.github.milesreimann.cloudsystem.master.domain.entity.DeploymentMetadataImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
@Mapper(config = MapStructConfig.class)
public abstract class DeploymentMetadataMapper {
    public DeploymentMetadata toDomain(JpaDeploymentMetadata entity) {
        Objects.requireNonNull(entity, "entity cannot be null");

        return new DeploymentMetadataImpl(
            entity.getId(),
            entity.getDeploymentType(),
            entity.getKey(),
            entity.getValue()
        );
    }

    @Mapping(target = "serverTemplate", ignore = true)
    protected abstract JpaDeploymentMetadata toEntity(DeploymentMetadata domain);
}