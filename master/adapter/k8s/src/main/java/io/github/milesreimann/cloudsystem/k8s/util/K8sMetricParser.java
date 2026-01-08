package io.github.milesreimann.cloudsystem.k8s.util;

import io.fabric8.kubernetes.api.model.Quantity;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class K8sMetricParser {
    private static final String RESOURCE_CPU = "cpu";
    private static final String RESOURCE_MEMORY = "memory";

    private K8sMetricParser() {}

    public static Resources parseResources(Map<String, Quantity> resourceMap) {
        if (resourceMap == null) {
            return ResourcesImpl.empty();
        }

        Quantity cpu = resourceMap.get(RESOURCE_CPU);
        Quantity memory = resourceMap.get(RESOURCE_MEMORY);

        return new ResourcesImpl(
            parseCpu(cpu),
            parseMemory(memory)
        );
    }

    private static double parseCpu(Quantity cpu) {
        return cpu != null && cpu.getAmount() != null
            ? cpu.getNumericalAmount().doubleValue()
            : 0D;
    }

    private static double parseMemory(Quantity memory) {
        if (memory == null || memory.getAmount() == null) {
            return 0D;
        }

        return Quantity.getAmountInBytes(memory)
            .divide(BigDecimal.valueOf(1024 * 1024))
            .doubleValue();
    }
}
