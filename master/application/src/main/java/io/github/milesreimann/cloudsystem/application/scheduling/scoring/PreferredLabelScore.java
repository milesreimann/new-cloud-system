package io.github.milesreimann.cloudsystem.application.scheduling.scoring;

import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.Label;

import java.util.Set;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public class PreferredLabelScore implements NodeScoringStrategy {
    @Override
    public double score(Node node, ServerTemplate serverTemplate) {
        Set<String> preferredNodeLabels = serverTemplate.getGroup().getPreferredNodeLabels();

        if (preferredNodeLabels == null || preferredNodeLabels.isEmpty()) {
            return 0.0D;
        }

        long matches = node.getLabels().stream()
            .map(Label::getKey)
            .filter(preferredNodeLabels::contains)
            .count();

        return (double) matches / preferredNodeLabels.size();
    }

    @Override
    public int getPriority() {
        return 200;
    }
}
