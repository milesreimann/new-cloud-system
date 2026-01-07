package io.github.milesreimann.cloudsystem.k8s.repository;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.k8s.mapper.K8sNodeMapper;

import java.util.List;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class K8sNodeRepository implements NodeRepository {
    private final KubernetesClient kubernetesClient;
    private final K8sNodeMapper nodeMapper;

    public K8sNodeRepository(KubernetesClient kubernetesClient, K8sNodeMapper nodeMapper) {
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