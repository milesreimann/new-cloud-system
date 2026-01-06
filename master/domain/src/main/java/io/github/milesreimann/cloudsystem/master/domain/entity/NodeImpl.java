package io.github.milesreimann.cloudsystem.master.domain.entity;

import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.api.model.Label;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.Objects;
import java.util.Set;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
public class NodeImpl implements Node {
    private final String name;
    private final String hostname;
    private final String ipAddress;
    private final NodeStatus status;
    private final Set<Label> labels;
    private final Resources capacity;
    private Resources usage;

    public NodeImpl(
        String name,
        String hostname,
        String ipAddress,
        NodeStatus status,
        Set<Label> labels,
        Resources capacity,
        Resources usage
    ) {
        this.name = name;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.status = status;
        this.labels = labels;
        this.capacity = capacity;
        this.usage = usage;
    }

    @Override
    public boolean hasLabel(Label label) {
        return labels.contains(label);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public NodeStatus getStatus() {
        return status;
    }

    @Override
    public Set<Label> getLabels() {
        return labels;
    }

    @Override
    public Resources getCapacity() {
        return capacity;
    }

    @Override
    public Resources getUsage() {
        return usage;
    }

    @Override
    public void updateUsage(Resources usage) {
        this.usage = usage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NodeImpl node = (NodeImpl) o;
        return Objects.equals(name, node.name) && Objects.equals(hostname, node.hostname) && Objects.equals(ipAddress, node.ipAddress) && status == node.status && Objects.equals(labels, node.labels) && Objects.equals(capacity, node.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, hostname, ipAddress, status, labels, capacity);
    }

    @Override
    public String toString() {
        return "NodeImpl{" +
            "name='" + name + '\'' +
            ", hostname='" + hostname + '\'' +
            ", ipAddress='" + ipAddress + '\'' +
            ", status=" + status +
            ", labels=" + labels +
            ", capacity=" + capacity +
            ", usage=" + usage +
            '}';
    }
}