package io.github.milesreimann.cloudsystem.adapter.framework.spring.mapper;

import io.github.milesreimann.cloudsystem.adapter.framework.spring.persistence.model.EmbeddedResources;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;
import org.mapstruct.Mapper;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
@Mapper(config = MapStructConfig.class, uses = {CPUMapper.class, MemoryMapper.class})
public interface ResourcesMapper {
    ResourcesImpl toDomain(EmbeddedResources embedded);

    EmbeddedResources toEmbedded(Resources domain);
}