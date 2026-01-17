package io.github.milesreimann.cloudsystem.adapter.framework.spring.persistence.model;

import io.github.milesreimann.cloudsystem.api.model.MemoryUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miles R.
 * @since 10.01.2026
 */
@Embeddable
@Data
@NoArgsConstructor
public class EmbeddedMemory {
    @Column(name = "value", nullable = false)
    private long value;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private MemoryUnit unit;
}