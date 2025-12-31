package io.github.milesreimann.cloudsystem.master.adapter.persistence.model;

import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.master.domain.model.AbstractResources;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
@Embeddable
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmbeddedResources extends AbstractResources {
    @Column(name = "cpu", nullable = false)
    private double cpu;

    @Column(name = "memory", nullable = false)
    private double memory;

    @Override
    protected Resources createNew(double cpu, double memory) {
        return new ResourcesImpl(cpu, memory);
    }
}
