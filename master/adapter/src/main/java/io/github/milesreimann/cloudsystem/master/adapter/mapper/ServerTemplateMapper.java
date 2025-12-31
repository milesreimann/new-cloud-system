package io.github.milesreimann.cloudsystem.master.adapter.mapper;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.master.adapter.persistence.entity.JpaServerTemplate;
import io.github.milesreimann.cloudsystem.master.domain.entity.ServerTemplateImpl;
import org.mapstruct.Mapper;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
@Mapper(componentModel = "spring")
public interface ServerTemplateMapper {
    default ServerTemplate toDomain(JpaServerTemplate entity) {
        return new ServerTemplateImpl(
            entity.getId(),
            entity.getAbbreviation(),
            entity.getName(),
            entity.getGroup(),
            entity.getMinServers(),
            entity.getMaxServers(),
            entity.getRequirements(),
            entity.isActive()
        );
    }

    JpaServerTemplate toEntity(ServerTemplate serverTemplate);
}
