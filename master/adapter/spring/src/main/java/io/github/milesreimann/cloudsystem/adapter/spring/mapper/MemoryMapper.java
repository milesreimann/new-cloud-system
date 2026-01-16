package io.github.milesreimann.cloudsystem.adapter.spring.mapper;

import io.github.milesreimann.cloudsystem.adapter.spring.persistence.model.EmbeddedMemory;
import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.master.domain.model.MemoryImpl;
import org.mapstruct.Mapper;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
@Mapper(config = MapStructConfig.class)
public interface MemoryMapper {
    default Memory toDomain(EmbeddedMemory embedded) {
        if (embedded == null) {
            return null;
        }

        return new MemoryImpl(embedded.getValue(), embedded.getUnit());
    }

    EmbeddedMemory toEmbedded(Memory domain);
}