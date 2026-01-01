package io.github.milesreimann.cloudsystem.master.adapter.watcher;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcher;
import io.github.milesreimann.cloudsystem.master.adapter.event.NodeAddEventImpl;
import io.github.milesreimann.cloudsystem.master.adapter.event.NodeRemoveEventImpl;
import io.github.milesreimann.cloudsystem.master.adapter.event.NodeStatusChangeEventImpl;
import io.github.milesreimann.cloudsystem.master.adapter.mapper.NodeMapper;
import io.github.milesreimann.cloudsystem.api.entity.Node;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class K8sNodeWatcher implements Watcher<io.fabric8.kubernetes.api.model.Node>, NodeWatcher {
    private final ApplicationEventPublisher eventPublisher;
    private final NodeMapper nodeMapper;
    private final NodeCache nodeCache;
    private final KubernetesClient kubernetesClient;

    private Watch watch;

    @Override
    public void watch() {
        watch = kubernetesClient.nodes().watch(this);
    }

    @Override
    public void close() {
        if (watch != null) {
            watch.close();
            watch = null;
        }
    }

    @Override
    public void eventReceived(Action action, io.fabric8.kubernetes.api.model.Node resource) {
        Node node = nodeMapper.fromK8sNode(resource);

        switch (action) {
            case ADDED -> {
                nodeCache.put(node.getName(), node);
                eventPublisher.publishEvent(new NodeAddEventImpl(node));
                log.info("Node added: {}", node);
            }
            case MODIFIED -> {
                Node oldNode = nodeCache.put(node.getName(), node);

                if (oldNode != null) {
                    checkForStatusChange(node, oldNode);
                    log.debug("Node modified: {}", node);
                } else {
                    eventPublisher.publishEvent(new NodeAddEventImpl(node));
                    log.warn("Node modified but not in cache, treating as new: {}", node);
                }
            }
            case DELETED -> {
                if (nodeCache.remove(node.getName())) {
                    eventPublisher.publishEvent(new NodeRemoveEventImpl(node));
                    log.info("Node deleted: {}", node.getName());
                }
            }
            case ERROR -> log.error("Watcher received error for node: {}", node.getName());
        }
    }

    @Override
    public void onClose(WatcherException cause) {
        log.warn("Node watcher closed", cause);
    }

    private void checkForStatusChange(Node node, Node oldNode) {
        if (node.getStatus() != oldNode.getStatus()) {
            eventPublisher.publishEvent(new NodeStatusChangeEventImpl(node, oldNode.getStatus(), node.getStatus()));
        }
    }
}
