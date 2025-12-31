package io.github.milesreimann.cloudsystem.application.scheduling.filter;

import io.github.milesreimann.cloudsystem.api.entity.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.Label;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public class RequiredLabelStrategy implements NodeFilterStrategy {
    @Override
    public List<Node> filter(List<Node> candidates, ServerTemplate serverTemplate) {
        Set<String> requiredNodeLabels = serverTemplate.getGroup().getRequiredNodeLabels();

        if (requiredNodeLabels == null || requiredNodeLabels.isEmpty()) {
            return candidates;
        }

        return candidates.stream()
            .filter(node -> nodeMatchesLabels(node, requiredNodeLabels))
            .toList();
    }

    private boolean nodeMatchesLabels(Node node, Set<String> requiredNodeLabels) {
        if (requiredNodeLabels == null || requiredNodeLabels.isEmpty()) {
            return true;
        }

        return node.getLabels().stream()
            .map(Label::getKey)
            .collect(Collectors.toSet())
            .containsAll(requiredNodeLabels);
    }

    @Override
    public int getPriority() {
        return 200;
    }
}
