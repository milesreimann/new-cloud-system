package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.node;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.mapper.KubernetesNodeMapper;

import java.util.List;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class KubernetesNodeRepository implements NodeRepository {
    private final KubernetesClient kubernetesClient;
    private final KubernetesNodeMapper nodeMapper;

    public KubernetesNodeRepository(KubernetesClient kubernetesClient, KubernetesNodeMapper nodeMapper) {
        this.kubernetesClient = kubernetesClient;
        this.nodeMapper = nodeMapper;
    }

    @Override
    public List<Node> findAll() {
        return kubernetesClient.nodes().list().getItems().stream()
            .map(nodeMapper::toDomain)
            .toList();
    }
}