package io.github.milesreimann.cloudsystem.application.listener;

import io.github.milesreimann.cloudsystem.api.event.EventListener;
import io.github.milesreimann.cloudsystem.application.event.NodeCacheInitializedEvent;
import io.github.milesreimann.cloudsystem.application.service.ServerSchedulingService;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class NodeCacheInitializedListener implements EventListener<NodeCacheInitializedEvent> {
    private final ServerSchedulingService serverSchedulingService;

    public NodeCacheInitializedListener(ServerSchedulingService serverSchedulingService) {
        this.serverSchedulingService = serverSchedulingService;
    }

    @Override
    public void handle(NodeCacheInitializedEvent event) {
        serverSchedulingService.scheduleServerTemplates();
    }
}