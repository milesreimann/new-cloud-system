package io.github.milesreimann.cloudsystem.master.adapter.out;

import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetrics;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageProvider;
import io.github.milesreimann.cloudsystem.master.domain.model.EmptyResources;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
@Component
@RequiredArgsConstructor
public class K8sNodeUsageProvider implements NodeUsageProvider {
    private final KubernetesClient kubernetesClient;

    @Override
    public Resources getUsage(String nodeName) {
        NodeMetrics metrics = kubernetesClient.top().nodes().withName("").metrics(nodeName);

        if (metrics == null || metrics.getUsage() == null) {
            return new EmptyResources();
        }

        Quantity cpu = metrics.getUsage().get("cpu");
        Quantity memory = metrics.getUsage().get("memory");

        return new ResourcesImpl(
            Quantity.getAmountInBytes(cpu).doubleValue(),
            Quantity.getAmountInBytes(memory).doubleValue() / (1024.0 * 1024.0)
        );
    }
}
