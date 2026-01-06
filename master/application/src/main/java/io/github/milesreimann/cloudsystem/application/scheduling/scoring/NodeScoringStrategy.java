package io.github.milesreimann.cloudsystem.application.scheduling.scoring;

import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public interface NodeScoringStrategy {
    double score(Node node, ServerTemplate serverTemplate);

    int getPriority();
}
