package io.github.milesreimann.cloudsystem.adapter.framework.spring.persistence.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
@Embeddable
@Data
@NoArgsConstructor
public class EmbeddedResources {
    @Embedded
    private EmbeddedCPU cpu;

    @Embedded
    private EmbeddedMemory memory;
}