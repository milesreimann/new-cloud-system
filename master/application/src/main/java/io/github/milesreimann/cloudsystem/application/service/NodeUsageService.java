package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProvider;

import java.util.concurrent.Executor;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public class NodeUsageService {
    private final NodeCache nodeCache;
    private final NodeUsageProvider nodeUsageProvider;
    private final Executor executor;

    public NodeUsageService(NodeCache nodeCache, NodeUsageProvider nodeUsageProvider, Executor executor) {
        this.nodeCache = nodeCache;
        this.nodeUsageProvider = nodeUsageProvider;
        this.executor = executor;
    }

    public void refreshUsages() {
        nodeCache.values().forEach(node -> executor.execute(() -> {
            Resources nodeUsage = nodeUsageProvider.getUsage(node.getName());
            node.updateUsage(nodeUsage);
        }));
    }
}
