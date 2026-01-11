package io.github.milesreimann.cloudsystem.k8s.util;

import io.fabric8.kubernetes.api.model.Quantity;
import io.github.milesreimann.cloudsystem.api.model.CPU;
import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.api.model.MemoryUnit;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.master.domain.model.CPUImpl;
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

        Quantity cpuQuantity = resourceMap.get(RESOURCE_CPU);
        Quantity memoryQuantity = resourceMap.get(RESOURCE_MEMORY);

        CPU cpu = parseCpu(cpuQuantity);
        Memory memory = parseMemory(memoryQuantity);

        return new ResourcesImpl(cpu, memory);
    }

    private static CPU parseCpu(Quantity cpuQuantity) {
        if (cpuQuantity == null || cpuQuantity.getAmount() == null) {
            return CPUImpl.empty();
        }

        double cores = cpuQuantity.getNumericalAmount().doubleValue();
        long millicores = (long) (cores * 1000);

        return new CPUImpl(millicores);
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
        } else if ("Mi".equalsIgnoreCase(format)) {
            unit = MemoryUnit.MI;
        } else {
            unit = MemoryUnit.B;
        }

        long bytes = Quantity.getAmountInBytes(memory).longValue();
        long valueInUnit = unit.fromBytes(bytes);

        return new MemoryImpl(valueInUnit, unit);
    }
}
