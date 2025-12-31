package io.github.milesreimann.cloudsystem.master.adapter.mapper;

import io.fabric8.kubernetes.api.model.NodeAddress;
import io.fabric8.kubernetes.api.model.Quantity;
import io.github.milesreimann.cloudsystem.master.domain.entity.NodeImpl;
import io.github.milesreimann.cloudsystem.master.domain.model.EmptyResources;
import io.github.milesreimann.cloudsystem.master.domain.model.LabelImpl;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;
import io.github.milesreimann.cloudsystem.api.entity.Node;
import io.github.milesreimann.cloudsystem.api.model.Label;
import io.github.milesreimann.cloudsystem.api.model.NodeStatus;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
@Component
public class NodeMapper {
    private static final String STATUS_CONDITION_TYPE = "Ready";
    private static final String STATUS_ONLINE_VALUE = "True";

    public Node fromK8sNode(io.fabric8.kubernetes.api.model.Node k8sNode) {
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
            new EmptyResources()
        );
    }

    private String parseIpAddress(io.fabric8.kubernetes.api.model.Node k8sNode) {
        return k8sNode.getStatus().getAddresses().stream()
            .filter(a -> a.getType().equals("InternalIP"))
            .map(NodeAddress::getAddress)
            .findFirst()
            .orElse("0.0.0.0");
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

        boolean isOnline = k8sNodeStatus.getConditions().stream()
            .anyMatch(condition -> condition.getType().equals(STATUS_CONDITION_TYPE) && condition.getStatus().equals(STATUS_ONLINE_VALUE));

        if (!isOnline) {
            return NodeStatus.OFFLINE;
        }

        boolean unschedulable = k8sNode.getSpec() != null && Boolean.TRUE.equals(k8sNode.getSpec().getUnschedulable());
        if (unschedulable) {
            return NodeStatus.DRAINING;
        }

        return NodeStatus.READY;
    }

    private Resources parseCapacity(io.fabric8.kubernetes.api.model.Node k8sNode) {
        if (k8sNode.getStatus().getCapacity() == null) {
            return new EmptyResources();
        }

        Quantity cpu = k8sNode.getStatus().getCapacity().get("cpu");
        Quantity memory = k8sNode.getStatus().getCapacity().get("memory");

        return new ResourcesImpl(parseCpu(cpu), parseMemory(memory));
    }

    private double parseCpu(Quantity cpu) {
        return cpu != null && cpu.getAmount() != null
            ? Double.parseDouble(cpu.getAmount())
            : -1D;
    }

    private double parseMemory(Quantity memory) {
        if (memory == null || memory.getAmount() == null) {
            return 0;
        }

        double bytes = Double.parseDouble(memory.getAmount());
        return bytes / (1024.0 * 1024.0);
    }
}
