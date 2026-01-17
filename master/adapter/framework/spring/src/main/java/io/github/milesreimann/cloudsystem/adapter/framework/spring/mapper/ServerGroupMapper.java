package io.github.milesreimann.cloudsystem.adapter.framework.spring.mapper;

import io.github.milesreimann.cloudsystem.adapter.framework.spring.persistence.entity.JpaServerGroup;
import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.master.domain.entity.ServerGroupImpl;
import org.mapstruct.Mapper;

import java.util.Objects;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
@Mapper(config = MapStructConfig.class)
public abstract class ServerGroupMapper {
    public ServerGroup toDomain(JpaServerGroup entity) {
        Objects.requireNonNull(entity, "entity cannot be null");

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

    protected abstract JpaServerGroup toEntity(ServerGroup domain);
}