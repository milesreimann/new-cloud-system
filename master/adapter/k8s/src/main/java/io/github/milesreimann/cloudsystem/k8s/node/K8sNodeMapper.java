package io.github.milesreimann.cloudsystem.k8s.node;

import io.fabric8.kubernetes.api.model.NodeAddress;
import io.github.milesreimann.cloudsystem.api.model.Label;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.mapper.NodeMapper;
import io.github.milesreimann.cloudsystem.k8s.util.K8sMetricParser;
import io.github.milesreimann.cloudsystem.master.domain.entity.NodeImpl;
import io.github.milesreimann.cloudsystem.master.domain.model.LabelImpl;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public class K8sNodeMapper implements NodeMapper<io.fabric8.kubernetes.api.model.Node> {
    private static final String STATUS_CONDITION_TYPE = "Ready";
    private static final String STATUS_ONLINE_VALUE = "True";
    private static final String ADDRESS_TYPE_INTERNAL_IP = "InternalIP";
    private static final String DEFAULT_IP_ADDRESS = "0.0.0.0";

    @Override
    public Node toDomain(io.fabric8.kubernetes.api.model.Node k8sNode) {
        String name = k8sNode.getMetadata().getName();
        String hostname = k8sNode.getMetadata().getName();
        String ipAddress = parseIpAddress(k8sNode);
        Set<Label> labels = parseLabels(k8sNode.getMetadata().getLabels());
        NodeStatus status = parseStatus(k8sNode);
        Resources capacity = parseCapacity(k8sNode);

        return new NodeImpl(
            name,
            hostname,
            ipAddress,
            status,
            labels,
            capacity,
            ResourcesImpl.empty()
        );
    }

    private String parseIpAddress(io.fabric8.kubernetes.api.model.Node k8sNode) {
        if (k8sNode.getStatus() == null) {
            return DEFAULT_IP_ADDRESS;
        }

        return k8sNode.getStatus().getAddresses().stream()
            .filter(a -> a.getType().equals(ADDRESS_TYPE_INTERNAL_IP))
            .map(NodeAddress::getAddress)
            .findFirst()
            .orElse(DEFAULT_IP_ADDRESS);
    }

    private Set<Label> parseLabels(Map<String, String> k8sLabels) {
        Set<Label> labels = new HashSet<>();
        if (k8sLabels != null) {
            k8sLabels.forEach((key, value) -> labels.add(LabelImpl.of(key, value)));
        }

        return labels;
    }

    private NodeStatus parseStatus(io.fabric8.kubernetes.api.model.Node k8sNode) {
        io.fabric8.kubernetes.api.model.NodeStatus k8sNodeStatus = k8sNode.getStatus();
        if (k8sNodeStatus == null || k8sNodeStatus.getConditions() == null) {
            return NodeStatus.UNKNOWN;
        }

        if (!isNodeReady(k8sNodeStatus)) {
            return NodeStatus.OFFLINE;
        }

        boolean hasNoScheduleTaint = k8sNode.getSpec() != null
            && k8sNode.getSpec().getTaints() != null
            && k8sNode.getSpec().getTaints().stream()
            .anyMatch(taint -> "NoSchedule".equals(taint.getEffect()));

        if (hasNoScheduleTaint) {
            return NodeStatus.NO_SCHEDULE;
        }

        boolean unschedulable = k8sNode.getSpec() != null && Boolean.TRUE.equals(k8sNode.getSpec().getUnschedulable());
        if (unschedulable) {
            return NodeStatus.DRAINING;
        }

        return NodeStatus.READY;
    }

    private boolean isNodeReady(io.fabric8.kubernetes.api.model.NodeStatus k8sNodeStatus) {
        return k8sNodeStatus.getConditions().stream()
            .anyMatch(condition -> STATUS_CONDITION_TYPE.equals(condition.getType())
                && STATUS_ONLINE_VALUE.equals(condition.getStatus()));
    }

    private Resources parseCapacity(io.fabric8.kubernetes.api.model.Node k8sNode) {
        io.fabric8.kubernetes.api.model.NodeStatus k8sNodeStatus = k8sNode.getStatus();
        if (k8sNodeStatus == null || k8sNodeStatus.getCapacity() == null) {
            return ResourcesImpl.empty();
        }

        return K8sMetricParser.parseResources(k8sNodeStatus.getCapacity());
    }
}
