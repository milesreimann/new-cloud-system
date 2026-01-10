package io.github.milesreimann.cloudsystem.adapter.spring.mapper;

import io.github.milesreimann.cloudsystem.adapter.spring.persistence.model.EmbeddedResources;
import io.github.milesreimann.cloudsystem.api.entity.DeploymentMetadata;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.adapter.spring.persistence.entity.JpaServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.master.domain.entity.ServerTemplateImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
@Mapper(componentModel = "spring")
public interface ServerTemplateMapper {
    @Mapping(target = "limits", source = "limits")
    default ServerTemplate toDomain(JpaServerTemplate entity) {
        List<DeploymentMetadata> deploymentMetadata = entity.getDeploymentMetadata().stream()
            .map(metadata  -> (DeploymentMetadata) metadata)
            .toList();

        return new ServerTemplateImpl(
            entity.getId(),
            entity.getAbbreviation(),
            entity.getName(),
            entity.getGroup(),
            entity.getMinServers(),
            entity.getMaxServers(),
            entity.getRequirements(),
            entity.getLimits().orElse(null),
            deploymentMetadata,
            entity.getEnvironmentVariables(),
            entity.isActive()
        );
    }

    @Mapping(target = "limits", source = "limits")
    JpaServerTemplate toEntity(ServerTemplate serverTemplate);

    default Optional<Resources> toDomainResources(EmbeddedResources embeddedResources) {
        return Optional.ofNullable(embeddedResources);
    }

    default EmbeddedResources toEntityResources(Optional<Resources> resources) {
        return resources.map(r -> (EmbeddedResources) r).orElse(null);
    }
}
