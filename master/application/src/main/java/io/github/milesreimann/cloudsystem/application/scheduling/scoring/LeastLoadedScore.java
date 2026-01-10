package io.github.milesreimann.cloudsystem.application.scheduling.scoring;

import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;

/**
 * @author Miles R.
 * @since 30.12.2025
 */
public class LeastLoadedScore implements NodeScoringStrategy {
    private final float cpuWeight;
    private final float memoryWeight;

    public LeastLoadedScore(float cpuWeight, float memoryWeight) {
        if (cpuWeight < 0 || memoryWeight < 0) {
            throw new IllegalArgumentException("Weights must be non-negative");
        }

        float sum = cpuWeight + memoryWeight;
        if (sum == 0) {
            throw new IllegalArgumentException("At least one weight must be positive");
        }

        this.cpuWeight = cpuWeight / sum;
        this.memoryWeight = memoryWeight / sum;
    }

    @Override
    public double score(Node node, ServerTemplate serverTemplate) {
        double cpuCapacity = node.getCapacity().getCpu();
        long memoryCapacityBytes = node.getCapacity().getMemory().toBytes();

        if (cpuCapacity <= 0D || memoryCapacityBytes <= 0D) {
            return 0.0D;
        }

        double cpuLoad = node.getUsage().getCpu() / cpuCapacity;
        double memoryLoad = (double) node.getUsage().getMemory().toBytes() / memoryCapacityBytes;

        double load = (cpuLoad * cpuWeight) + (memoryLoad * memoryWeight);

        return Math.max(0.0, 1.0 - load);
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
