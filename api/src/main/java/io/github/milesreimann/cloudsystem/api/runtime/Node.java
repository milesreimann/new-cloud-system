package io.github.milesreimann.cloudsystem.api.runtime;

import io.github.milesreimann.cloudsystem.api.model.Label;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.Set;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public interface Node {
    String getName();

    String getHostname();

    String getIpAddress();

    NodeStatus getStatus();

    Set<Label> getLabels();

    Resources getCapacity();

    Resources getUsage();

    void updateUsage(Resources usage);

    boolean hasLabel(Label label);
}
