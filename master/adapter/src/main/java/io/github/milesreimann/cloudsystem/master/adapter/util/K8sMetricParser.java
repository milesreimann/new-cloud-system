package io.github.milesreimann.cloudsystem.master.adapter.util;

import io.fabric8.kubernetes.api.model.Quantity;

import java.math.BigDecimal;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class K8sMetricParser {
    private K8sMetricParser() {}

    public static double parseCpu(Quantity cpu) {
        return cpu != null && cpu.getAmount() != null
            ? cpu.getNumericalAmount().doubleValue()
            : 0D;
    }

    public static double parseMemory(Quantity memory) {
        if (memory == null || memory.getAmount() == null) {
            return 0D;
        }

        return Quantity.getAmountInBytes(memory)
            .divide(BigDecimal.valueOf(1024 * 1024))
            .doubleValue();
    }
}
