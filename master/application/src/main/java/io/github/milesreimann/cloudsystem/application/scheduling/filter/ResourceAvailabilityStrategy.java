package io.github.milesreimann.cloudsystem.application.scheduling.filter;

import io.github.milesreimann.cloudsystem.api.entity.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.List;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public class ResourceAvailabilityStrategy implements NodeFilterStrategy {
    private final int safetyMarginFactor;

    public ResourceAvailabilityStrategy(int safetyMarginFactor) {
        this.safetyMarginFactor = safetyMarginFactor;
    }

    @Override
    public List<Node> filter(List<Node> candidates, ServerTemplate serverTemplate) {
        return candidates.stream()
            .filter(node -> hasEnoughResources(node, serverTemplate))
            .toList();
    }

    private boolean hasEnoughResources(Node node, ServerTemplate template) {
        Resources available = node.getCapacity().subtract(node.getUsage());
        return fitsWithMargin(available, template.getRequirements());
    }

    private boolean fitsWithMargin(Resources resources, Resources required) {
        double requiredCpu = required.getCpu() * (1.0 + safetyMarginFactor);
        double requiredMemory = required.getMemory() * (1.0 + safetyMarginFactor);

        return resources.getCpu() >= requiredCpu
            && resources.getMemory() >= requiredMemory;
    }

    @Override
    public int getPriority() {
        return 300;
    }
}
