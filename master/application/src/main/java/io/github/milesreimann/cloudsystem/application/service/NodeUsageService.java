package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProviderPort;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageSchedulerPort;

import java.util.concurrent.Executor;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public class NodeUsageService {
    private final NodeCache nodeCache;
    private final NodeUsageProviderPort nodeUsageProviderPort;
    private final Executor executor;

    public NodeUsageService(
        NodeCache nodeCache,
        NodeUsageProviderPort nodeUsageProviderPort,
        NodeUsageSchedulerPort nodeUsageSchedulerPort,
        Executor executor
    ) {
        this.nodeCache = nodeCache;
        this.nodeUsageProviderPort = nodeUsageProviderPort;
        this.executor = executor;

        nodeUsageSchedulerPort.schedule();
    }

    public void refreshUsages() {
        nodeCache.values().forEach(node -> executor.execute(() -> {
            Resources nodeUsage = nodeUsageProviderPort.getUsage(node.getName());
            node.updateUsage(nodeUsage);
        }));
    }
}