package io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter;

import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;

import java.util.List;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public interface NodeFilterStrategy {
    List<Node> filter(List<Node> candidates, ServerTemplate serverTemplate);

    int getPriority();
}