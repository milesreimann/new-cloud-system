package io.github.milesreimann.cloudsystem.adapter.spring.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.application.port.out.NodeRepository;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeMapper;
import io.github.milesreimann.cloudsystem.k8s.node.K8sNodeRepository;
import io.github.milesreimann.cloudsystem.k8s.server.K8sServerDeployment;
import io.github.milesreimann.cloudsystem.k8s.server.K8sServerWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Configuration
public class KubernetesConfig {
    @Bean
    public K8sNodeMapper nodeMapper() {
        return new K8sNodeMapper();
    }

    @Bean
    public ServerDeploymentPort serverDeploymentPort(KubernetesClient kubernetesClient) {
        return new K8sServerDeployment(kubernetesClient);
    }

    @Bean
    public NodeRepository nodeRepository(KubernetesClient kubernetesClient, K8sNodeMapper nodeMapper) {
        return new K8sNodeRepository(kubernetesClient, nodeMapper);
    }

    @Bean
    public K8sServerWatcher serverWatcher(KubernetesClient kubernetesClient) {
        return new K8sServerWatcher(kubernetesClient);
    }
}