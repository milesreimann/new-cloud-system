package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProvider;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageScheduler;

import java.util.concurrent.Executor;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public class NodeUsageService {
    private final NodeCache nodeCache;
    private final NodeUsageProvider nodeUsageProvider;
    private final NodeUsageScheduler nodeUsageScheduler;
    private final Executor executor;

    public NodeUsageService(
        NodeCache nodeCache,
        NodeUsageProvider nodeUsageProvider,
        NodeUsageScheduler nodeUsageScheduler,
        Executor executor
    ) {
        this.nodeCache = nodeCache;
        this.nodeUsageProvider = nodeUsageProvider;
        this.nodeUsageScheduler = nodeUsageScheduler;
        this.executor = executor;

        nodeUsageScheduler.schedule();
    }

    public void refreshUsages() {
        nodeCache.values().forEach(node -> executor.execute(() -> {
            Resources nodeUsage = nodeUsageProvider.getUsage(node.getName());
            node.updateUsage(nodeUsage);
        }));
    }
}
