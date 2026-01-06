package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.event.EventBus;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.event.NodeCacheInitializedEvent;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class NodeInitializationService {
    private final Logger log = LoggerFactory.getLogger(NodeInitializationService.class);

    private final NodeRepository nodeRepository;
    private final NodeWatcher nodeWatcher;
    private final NodeCache nodeCache;
    private final EventBus eventBus;

    public NodeInitializationService(
        NodeRepository nodeRepository,
        NodeWatcher nodeWatcher,
        NodeCache nodeCache,
        EventBus eventBus
    ) {
        this.nodeRepository = nodeRepository;
        this.nodeWatcher = nodeWatcher;
        this.nodeCache = nodeCache;
        this.eventBus = eventBus;
    }

    public void initialize() {
        log.info("Initializing node cache...");

        List<Node> nodes = nodeRepository.findAll();

        nodes.forEach(node -> {
            nodeCache.put(node.getName(), node);
            log.info("Loaded node: {}", node.getName());
        });

        log.info("Node cache initialized, starting watcher");
        nodeWatcher.watch();

        eventBus.publish(new NodeCacheInitializedEvent());
    }
}
