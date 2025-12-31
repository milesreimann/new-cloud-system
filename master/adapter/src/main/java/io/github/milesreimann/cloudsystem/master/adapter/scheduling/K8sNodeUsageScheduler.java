package io.github.milesreimann.cloudsystem.master.adapter.scheduling;

import io.github.milesreimann.cloudsystem.application.service.NodeUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
@Component
@RequiredArgsConstructor
public class K8sNodeUsageScheduler {
    private final NodeUsageService nodeUsageService;

    @Scheduled(fixedDelayString = "${node.usage.refresh-ms}")
    public void tick() {
        nodeUsageService.refreshUsages();
    }
}
