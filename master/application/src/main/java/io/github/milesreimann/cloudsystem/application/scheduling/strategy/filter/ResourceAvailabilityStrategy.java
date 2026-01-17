package io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter;

import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.List;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public class ResourceAvailabilityStrategy implements NodeFilterStrategy {
    @Override
    public List<Node> filter(List<Node> candidates, ServerTemplate serverTemplate) {
        return candidates.stream()
            .filter(node -> hasEnoughResources(node, serverTemplate))
            .toList();
    }

    private boolean hasEnoughResources(Node node, ServerTemplate template) {
        Resources available = node.getCapacity().subtract(node.getUsage());
        Resources required = template.getRequirements();

        return available.fits(required);
    }

    @Override
    public int getPriority() {
        return 300;
    }
}
