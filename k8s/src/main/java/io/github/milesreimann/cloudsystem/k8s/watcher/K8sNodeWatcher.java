package io.github.milesreimann.cloudsystem.k8s.watcher;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.port.out.NodeWatcher;
import io.github.milesreimann.cloudsystem.k8s.mapper.K8sNodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public class K8sNodeWatcher implements Watcher<io.fabric8.kubernetes.api.model.Node>, NodeWatcher {
    private static final Logger LOG = LoggerFactory.getLogger(K8sNodeWatcher.class);

    private final K8sNodeMapper nodeMapper;
    private final NodeCache nodeCache;
    private final KubernetesClient kubernetesClient;

    private Watch watch;

    public K8sNodeWatcher(
        K8sNodeMapper nodeMapper,
        NodeCache nodeCache,
        KubernetesClient kubernetesClient
    ) {
        this.nodeMapper = nodeMapper;
        this.nodeCache = nodeCache;
        this.kubernetesClient = kubernetesClient;
    }

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
        Node node = nodeMapper.toCloudNode(resource);

        switch (action) {
            case ADDED -> {
                if (!nodeCache.contains(node.getName())) {
                    nodeCache.put(node.getName(), node);
                    LOG.info("Node added: {}", node);
                }
            }
            case MODIFIED -> {
                Node oldNode = nodeCache.put(node.getName(), node);

                if (oldNode != null) {
                    LOG.debug("Node modified: {}", node);
                } else {
                    LOG.warn("Node modified but not in cache, treating as new: {}", node);
                }
            }
            case DELETED -> {
                if (nodeCache.remove(node.getName())) {
                    LOG.info("Node deleted: {}", node.getName());
                }
            }
            case ERROR -> LOG.error("Watcher received error for node: {}", node.getName());
        }
    }

    @Override
    public void onClose(WatcherException cause) {
        LOG.warn("Node watcher closed", cause);
    }
}
