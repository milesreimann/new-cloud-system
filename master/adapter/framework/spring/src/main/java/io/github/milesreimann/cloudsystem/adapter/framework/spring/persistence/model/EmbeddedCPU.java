package io.github.milesreimann.cloudsystem.adapter.framework.spring.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
@Embeddable
@Data
@NoArgsConstructor
public class EmbeddedCPU {
    @Column(name = "millicores", nullable = false)
    private long millicores;
}
