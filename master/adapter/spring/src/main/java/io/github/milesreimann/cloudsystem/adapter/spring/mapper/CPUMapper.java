package io.github.milesreimann.cloudsystem.adapter.spring.mapper;

import io.github.milesreimann.cloudsystem.adapter.spring.persistence.model.EmbeddedCPU;
import io.github.milesreimann.cloudsystem.api.model.CPU;
import io.github.milesreimann.cloudsystem.master.domain.model.CPUImpl;
import org.mapstruct.Mapper;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
@Mapper(config = MapStructConfig.class)
public interface CPUMapper {
    default CPU toDomain(EmbeddedCPU embedded) {
        if (embedded == null) {
            return null;
        }

        return new CPUImpl(embedded.getMillicores());
    }

    EmbeddedCPU toEmbedded(CPU domain);
}