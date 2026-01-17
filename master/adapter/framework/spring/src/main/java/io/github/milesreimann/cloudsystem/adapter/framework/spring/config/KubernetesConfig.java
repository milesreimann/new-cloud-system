package io.github.milesreimann.cloudsystem.adapter.framework.spring.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.mapper.ResourceRequirementsMapper;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.pod.InitContainerSpec;
import io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.pod.ServerPodBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
@Configuration
public class KubernetesConfig {
    @Bean
    public KubernetesClient kubernetesClient() {
        return new KubernetesClientBuilder().build();
    }

    @Bean
    public ServerPodBuilder serverPodBuilder(
        ResourceRequirementsMapper resourceRequirementsMapper,
        InitContainerSpec initContainerSpec
    ) {
        return new ServerPodBuilder(resourceRequirementsMapper, initContainerSpec);
    }

    @Bean
    public ResourceRequirementsMapper resourceRequirementsMapper() {
        return new ResourceRequirementsMapper();
    }

    @Bean
    public InitContainerSpec initContainerSpec() {
        return new InitContainerSpec();
    }
}