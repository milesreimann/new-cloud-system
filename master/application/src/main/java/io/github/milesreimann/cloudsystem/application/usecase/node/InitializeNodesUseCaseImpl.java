package io.github.milesreimann.cloudsystem.application.usecase.node;

import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.in.node.InitializeNodesUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.node.ListNodesUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.server.ScheduleServersUseCase;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class InitializeNodesUseCaseImpl implements InitializeNodesUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(InitializeNodesUseCaseImpl.class);

    private final NodeRepository nodeRepository;
    private final NodeWatcherPort nodeWatcherPort;
    private final NodeCache nodeCache;
    private final ScheduleServersUseCase scheduleServersUseCase;

    public InitializeNodesUseCaseImpl(
        NodeRepository nodeRepository,
        NodeWatcherPort nodeWatcherPort,
        NodeCache nodeCache,
        ScheduleServersUseCase scheduleServersUseCase
    ) {
        this.nodeRepository = nodeRepository;
        this.nodeWatcherPort = nodeWatcherPort;
        this.nodeCache = nodeCache;
        this.scheduleServersUseCase = scheduleServersUseCase;
    }

    @Override
    public void initialize() {
        LOG.info("Initializing NodeCache...");

        List<Node> nodes = nodeRepository.findAll();
        LOG.debug("{} nodes found", nodes.size());

        nodes.forEach(node -> {
            nodeCache.put(node.getName(), node);
            LOG.info("Loaded node: {}", node.getName());
        });

        LOG.info("NodeCache initialized, starting NodeWatcher...");
        nodeWatcherPort.watch();

        LOG.info("NodeWatcher started, scheduling servers...");
        scheduleServersUseCase.scheduleAllActiveTemplates();
    }
}
