package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class NodeInitializationService {
    private static final Logger LOG = LoggerFactory.getLogger(NodeInitializationService.class);

    private final NodeRepository nodeRepository;
    private final NodeWatcherPort nodeWatcherPort;
    private final NodeCache nodeCache;
    private final ServerSchedulerService serverSchedulingService;

    public NodeInitializationService(
        NodeRepository nodeRepository,
        NodeWatcherPort nodeWatcherPort,
        NodeCache nodeCache,
        ServerSchedulerService serverSchedulingService
    ) {
        this.nodeRepository = nodeRepository;
        this.nodeWatcherPort = nodeWatcherPort;
        this.nodeCache = nodeCache;
        this.serverSchedulingService = serverSchedulingService;
    }

    public void initialize() {
        LOG.info("Initializing NodeCache...");

        List<Node> nodes = nodeRepository.findAll();

        nodes.forEach(node -> {
            nodeCache.put(node.getName(), node);
            LOG.info("Loaded node: {}", node.getName());
        });

        LOG.info("NodeCache initialized, starting NodeWatcher...");
        nodeWatcherPort.watch();

        LOG.info("NodeWatcher started, scheduling servers...");
        serverSchedulingService.scheduleActiveServerTemplates();
    }
}