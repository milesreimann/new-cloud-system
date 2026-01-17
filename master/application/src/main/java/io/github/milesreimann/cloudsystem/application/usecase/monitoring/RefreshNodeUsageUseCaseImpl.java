package io.github.milesreimann.cloudsystem.application.usecase.monitoring;

import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.in.monitoring.RefreshNodeUsageUseCase;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProviderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class RefreshNodeUsageUseCaseImpl implements RefreshNodeUsageUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(RefreshNodeUsageUseCaseImpl.class);

    private final NodeCache nodeCache;
    private final NodeUsageProviderPort usageProvider;

    public RefreshNodeUsageUseCaseImpl(NodeCache nodeCache, NodeUsageProviderPort usageProvider) {
        this.nodeCache = nodeCache;
        this.usageProvider = usageProvider;
    }

    @Override
    public void refreshAll() {
        LOG.debug("Refreshing usage for all nodes");

        Map<Node, CompletableFuture<Resources>> usageFutures = nodeCache.values().stream()
            .collect(Collectors.toMap(
                node -> node,
                node -> usageProvider.getUsage(node.getName())
            ));

        CompletableFuture.allOf(usageFutures.values().toArray(CompletableFuture[]::new))
            .thenRun(() ->
                usageFutures.forEach((node, usageFuture) -> refreshNodeUsage(node, usageFuture.join()))
            );
    }

    private void refreshNodeUsage(Node node, Resources usage) {
        try {
            node.updateUsage(usage);
            LOG.trace("Updated usage for node '{}': {}", node.getName(), usage);
        } catch (Exception e) {
            LOG.error("Failed to refresh usage for node '{}'", node.getName(), e);
        }
    }
}