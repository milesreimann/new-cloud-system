package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.mapper;

import io.fabric8.kubernetes.api.model.NodeAddress;
import io.github.milesreimann.cloudsystem.api.model.Label;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.mapper.NodeMapper;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.util.KubernetesMetricParser;
import io.github.milesreimann.cloudsystem.master.domain.runtime.NodeImpl;
import io.github.milesreimann.cloudsystem.master.domain.model.LabelImpl;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public class KubernetesNodeMapper implements NodeMapper<io.fabric8.kubernetes.api.model.Node> {
    private static final String STATUS_CONDITION_TYPE = "Ready";
    private static final String STATUS_ONLINE_VALUE = "True";
    private static final String ADDRESS_TYPE_INTERNAL_IP = "InternalIP";
    private static final String DEFAULT_IP_ADDRESS = "0.0.0.0";

    @Override
    public Node toDomain(io.fabric8.kubernetes.api.model.Node kubernetesNodeNode) {
        String name = kubernetesNodeNode.getMetadata().getName();
        String hostname = kubernetesNodeNode.getMetadata().getName();
        String ipAddress = parseIpAddress(kubernetesNodeNode);
        Set<Label> labels = parseLabels(kubernetesNodeNode.getMetadata().getLabels());
        NodeStatus status = parseStatus(kubernetesNodeNode);
        Resources capacity = parseCapacity(kubernetesNodeNode);

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

    private String parseIpAddress(io.fabric8.kubernetes.api.model.Node kubernetesNodeNode) {
        if (kubernetesNodeNode.getStatus() == null) {
            return DEFAULT_IP_ADDRESS;
        }

        return kubernetesNodeNode.getStatus().getAddresses().stream()
            .filter(a -> a.getType().equals(ADDRESS_TYPE_INTERNAL_IP))
            .map(NodeAddress::getAddress)
            .findFirst()
            .orElse(DEFAULT_IP_ADDRESS);
    }

    private Set<Label> parseLabels(Map<String, String> kubernetesNodeLabels) {
        Set<Label> labels = new HashSet<>();
        if (kubernetesNodeLabels != null) {
            kubernetesNodeLabels.forEach((key, value) -> labels.add(LabelImpl.of(key, value)));
        }

        return labels;
    }

    private NodeStatus parseStatus(io.fabric8.kubernetes.api.model.Node kubernetesNodeNode) {
        io.fabric8.kubernetes.api.model.NodeStatus kubernetesNodeNodeStatus = kubernetesNodeNode.getStatus();
        if (kubernetesNodeNodeStatus == null || kubernetesNodeNodeStatus.getConditions() == null) {
            return NodeStatus.UNKNOWN;
        }

        if (!isNodeReady(kubernetesNodeNodeStatus)) {
            return NodeStatus.OFFLINE;
        }

        boolean hasNoScheduleTaint = kubernetesNodeNode.getSpec() != null
            && kubernetesNodeNode.getSpec().getTaints() != null
            && kubernetesNodeNode.getSpec().getTaints().stream()
            .anyMatch(taint -> "NoSchedule".equals(taint.getEffect()));

        if (hasNoScheduleTaint) {
            return NodeStatus.NO_SCHEDULE;
        }

        boolean unschedulable = kubernetesNodeNode.getSpec() != null && Boolean.TRUE.equals(kubernetesNodeNode.getSpec().getUnschedulable());
        if (unschedulable) {
            return NodeStatus.DRAINING;
        }

        return NodeStatus.READY;
    }

    private boolean isNodeReady(io.fabric8.kubernetes.api.model.NodeStatus kubernetesNodeNodeStatus) {
        return kubernetesNodeNodeStatus.getConditions().stream()
            .anyMatch(condition -> STATUS_CONDITION_TYPE.equals(condition.getType())
                && STATUS_ONLINE_VALUE.equals(condition.getStatus()));
    }

    private Resources parseCapacity(io.fabric8.kubernetes.api.model.Node kubernetesNodeNode) {
        io.fabric8.kubernetes.api.model.NodeStatus kubernetesNodeNodeStatus = kubernetesNodeNode.getStatus();
        if (kubernetesNodeNodeStatus == null || kubernetesNodeNodeStatus.getCapacity() == null) {
            return ResourcesImpl.empty();
        }

        return KubernetesMetricParser.parseResources(kubernetesNodeNodeStatus.getCapacity());
    }
}
