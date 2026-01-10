package io.github.milesreimann.cloudsystem.k8s.util;

import io.fabric8.kubernetes.api.model.Quantity;
import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.api.model.MemoryUnit;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.master.domain.model.MemoryImpl;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;

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

    private static Memory parseMemory(Quantity memory) {
        if (memory == null || memory.getAmount() == null) {
            return MemoryImpl.empty();
        }

        String format = memory.getFormat();
        MemoryUnit unit;

        if ("Ki".equalsIgnoreCase(format)) {
            unit = MemoryUnit.KI;
        } else if ("Gi".equalsIgnoreCase(format)) {
            unit = MemoryUnit.GI;
        } else {
            unit = MemoryUnit.MI;
        }

        long bytes = Quantity.getAmountInBytes(memory).longValue();
        long valueInUnit = unit.fromBytes(bytes);

        return new MemoryImpl(valueInUnit, unit);
    }
}
