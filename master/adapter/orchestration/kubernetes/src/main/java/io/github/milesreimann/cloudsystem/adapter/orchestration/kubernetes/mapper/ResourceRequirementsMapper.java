package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.mapper;

import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.api.model.Resources;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class ResourceRequirementsMapper {
    private static final String RESOURCE_MEMORY = "memory";
    private static final String RESOURCE_CPU = "cpu";

    public ResourceRequirements toKubernetesResources(ServerTemplate template) {
        Resources requirements = template.getRequirements();
        Resources limits = template.getLimits().orElse(requirements);

        return new ResourceRequirementsBuilder()
            .addToRequests(RESOURCE_CPU, toCpuQuantity(requirements))
            .addToRequests(RESOURCE_MEMORY, toMemoryQuantity(requirements.getMemory()))
            .addToLimits(RESOURCE_CPU, toCpuQuantity(limits))
            .addToLimits(RESOURCE_MEMORY, toMemoryQuantity(limits.getMemory()))
            .build();
    }

    private Quantity toCpuQuantity(Resources resources) {
        return new Quantity(resources.getCpu().getMillicores() + resources.getCpu().getSuffix());
    }

    private Quantity toMemoryQuantity(Memory memory) {
        return new Quantity(memory.getValue() + memory.getUnit().getSuffix());
    }
}