package io.github.milesreimann.cloudsystem.master.adapter.mapper;

import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.master.adapter.persistence.entity.JpaServerGroup;
import io.github.milesreimann.cloudsystem.master.domain.entity.ServerGroupImpl;
import org.mapstruct.Mapper;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
@Mapper(componentModel = "spring")
public interface ServerGroupMapper {
    default ServerGroup toDomain(JpaServerGroup entity) {
        return new ServerGroupImpl(
            entity.getId(),
            entity.getAbbreviation(),
            entity.getName(),
            entity.getType(),
            entity.getRequiredNodeLabels(),
            entity.getPreferredNodeLabels(),
            entity.isActive()
        );
    }

    JpaServerGroup toEntity(ServerGroup serverGroup);
}
