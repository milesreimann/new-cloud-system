package io.github.milesreimann.cloudsystem.adapter.spring.mapper;

import io.github.milesreimann.cloudsystem.adapter.spring.persistence.entity.JpaDeploymentMetadata;
import io.github.milesreimann.cloudsystem.adapter.spring.persistence.model.EmbeddedResources;
import io.github.milesreimann.cloudsystem.api.entity.DeploymentMetadata;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.adapter.spring.persistence.entity.JpaServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.master.domain.entity.ServerTemplateImpl;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
@Mapper(
    config = MapStructConfig.class,
    uses = {ResourcesMapper.class, DeploymentMetadataMapper.class, ServerGroupMapper.class}
)
public abstract class ServerTemplateMapper {
    @Autowired
    protected ResourcesMapper resourcesMapper;

    @Autowired
    protected DeploymentMetadataMapper deploymentMetadataMapper;

    @Autowired
    protected ServerGroupMapper serverGroupMapper;

    public ServerTemplate toDomain(JpaServerTemplate entity) {
        Objects.requireNonNull(entity, "entity cannot be null");

        return new ServerTemplateImpl(
            entity.getId(),
            entity.getAbbreviation(),
            entity.getName(),
            serverGroupMapper.toDomain(entity.getGroup()),
            entity.getMinServers(),
            entity.getMaxServers(),
            resourcesMapper.toDomain(entity.getRequirements()),
            resourcesMapper.toDomain(entity.getLimits()),
            mapMetadata(entity.getDeploymentMetadata()),
            entity.getEnvironmentVariables(),
            entity.isActive()
        );
    }

    @Mapping(target = "deploymentMetadata", ignore = true)
    public abstract JpaServerTemplate toEntity(ServerTemplate domain);

    @AfterMapping
    protected void linkDeploymentMetadata(@MappingTarget JpaServerTemplate entity, ServerTemplate domain) {
        if (domain == null || domain.getDeploymentMetadata() == null) {
            return;
        }

        entity.getDeploymentMetadata().clear();

        domain.getDeploymentMetadata().forEach(metadata -> {
            JpaDeploymentMetadata jpaMetadata = deploymentMetadataMapper.toEntity(metadata);
            jpaMetadata.setServerTemplate(entity);
            entity.getDeploymentMetadata().add(jpaMetadata);
        });
    }

    private List<DeploymentMetadata> mapMetadata(List<JpaDeploymentMetadata> metadata) {
        if (metadata == null) {
            return List.of();
        }

        return metadata.stream()
            .map(deploymentMetadataMapper::toDomain)
            .toList();
    }

    protected EmbeddedResources map(Optional<Resources> optional) {
        return optional.map(resourcesMapper::toEmbedded)
            .orElse(null);
    }
}