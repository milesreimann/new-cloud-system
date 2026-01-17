package io.github.milesreimann.cloudsystem.adapter.framework.spring.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.pod.ServerPodBuilder;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.server.KubernetesServerDeployment;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.server.KubernetesServerWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Configuration
public class ServerConfig {
    @Bean
    public ServerDeploymentPort serverDeploymentPort(
        KubernetesClient kubernetesClient,
        ServerPodBuilder serverPodBuilder,
        Executor executor
    ) {
        return new KubernetesServerDeployment(kubernetesClient, serverPodBuilder, executor);
    }

    @Bean
    public KubernetesServerWatcher serverWatcher(KubernetesClient kubernetesClient) {
        return new KubernetesServerWatcher(kubernetesClient);
    }
}