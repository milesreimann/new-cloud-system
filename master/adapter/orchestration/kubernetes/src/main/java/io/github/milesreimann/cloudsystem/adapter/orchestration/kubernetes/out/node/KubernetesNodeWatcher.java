package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.node;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.exception.MaxReconnectAttemptsExceededException;
import io.github.milesreimann.cloudsystem.application.resilience.NodeWatcherRetryPolicy;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcherPort;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.mapper.KubernetesNodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public class KubernetesNodeWatcher implements Watcher<io.fabric8.kubernetes.api.model.Node>, NodeWatcherPort {
    private static final Logger LOG = LoggerFactory.getLogger(KubernetesNodeWatcher.class);

    private final KubernetesNodeMapper nodeMapper;
    private final NodeCache nodeCache;
    private final NodeWatcherRetryPolicy retryPolicy;
    private final KubernetesClient kubernetesClient;

    private final AtomicReference<Watch> watch = new AtomicReference<>();
    private final AtomicBoolean running = new AtomicBoolean(false);

    public KubernetesNodeWatcher(
        KubernetesNodeMapper nodeMapper,
        NodeCache nodeCache,
        NodeWatcherRetryPolicy retryPolicy,
        KubernetesClient kubernetesClient
    ) {
        this.nodeMapper = nodeMapper;
        this.nodeCache = nodeCache;
        this.retryPolicy = retryPolicy;
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public void watch() {
        if (running.compareAndSet(false, true)) {
            startWatch();
        } else {
            LOG.warn("Node watcher is already running");
        }
    }

    @Override
    public void close() {
        running.set(false);
        closeWatch();
    }

    @Override
    public void eventReceived(Action action, io.fabric8.kubernetes.api.model.Node resource) {
        if (resource == null) {
            LOG.warn("Received null resource in watcher event");
            return;
        }

        Node node = nodeMapper.toDomain(resource);

        switch (action) {
            case ADDED -> handleAdded(node);
            case MODIFIED -> handleModified(node);
            case DELETED -> handleDeleted(node);
            case ERROR -> handleError(node);
        }
    }

    @Override
    public void onClose(WatcherException cause) {
        closeWatch();

        if (!running.get()) {
            LOG.info("NodeWatcher closed gracefully");
            return;
        }

        if (cause != null) {
            LOG.error("NodeWatcher closed unexpectedly", cause);
        } else {
            LOG.warn("NodeWatcher closed without exception");
        }

        attemptReconnect();
    }

    private void startWatch() {
        try {
            Watch newWatch = kubernetesClient.nodes().watch(this);
            watch.set(newWatch);
            retryPolicy.reset();
            LOG.info("NodeWatcher started successfully");
        } catch (Exception e) {
            LOG.error("Failed to start NodeWatcher", e);
            running.set(false);
        }
    }

    private void closeWatch() {
        Watch currentWatch = watch.getAndSet(null);

        if (currentWatch != null) {
            try {
                currentWatch.close();
                LOG.info("NodeWatcher closed successfully");
            } catch (Exception e) {
                LOG.warn("Error closing NodeWatcher", e);
            }
        }
    }

    private void handleAdded(Node node) {
        if (!nodeCache.contains(node.getName())) {
            nodeCache.put(node.getName(), node);
            LOG.info("Node added: {}", node.getName());
            return;
        }

        LOG.debug("Node already exists in cache, updating: {}", node.getName());
        nodeCache.put(node.getName(), node);
    }

    private void handleModified(Node node) {
        Node oldNode = nodeCache.put(node.getName(), node);

        if (oldNode != null) {
            LOG.debug("Node modified: {}", node.getName());
        } else {
            LOG.warn("Node modified but not in cache, treating as new: {}", node.getName());
        }
    }

    private void handleDeleted(Node node) {
        if (nodeCache.remove(node.getName())) {
            LOG.info("Node deleted: {}", node.getName());
        } else {
            LOG.warn("Node deletion event for unknown node: {}", node.getName());
        }
    }

    private void handleError(Node node) {
        LOG.error("Watcher received ERROR event for node: {}", node.getName());
    }

    private void attemptReconnect() {
        if (!retryPolicy.shouldReconnect()) {
            running.set(false);
            return;
        }

        try {
            retryPolicy.attemptReconnect(this::startWatch);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.error("Reconnect interrupted", e);
            running.set(false);
        } catch (MaxReconnectAttemptsExceededException e) {
            LOG.error(e.getMessage());
            running.set(false);
        }
    }
}