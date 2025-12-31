package io.github.milesreimann.cloudsystem.application.cache;

import io.github.milesreimann.cloudsystem.api.entity.Node;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Miles R.
 * @since 27.12.2025
 */
public class NodeCache {
    private final Map<String, Node> nodes = new ConcurrentHashMap<>();

    public Node add(Node node) {
        return nodes.put(node.getName(), node);
    }

    public boolean remove(String nodeId) {
        return nodes.remove(nodeId) != null;
    }

    public Optional<Node> get(String nodeId) {
        return Optional.ofNullable(nodes.get(nodeId));
    }

    public Collection<Node> values() {
        return nodes.values();
    }
}