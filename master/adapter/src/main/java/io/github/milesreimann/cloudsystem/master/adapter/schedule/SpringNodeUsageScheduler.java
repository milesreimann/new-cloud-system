package io.github.milesreimann.cloudsystem.master.adapter.schedule;

import io.github.milesreimann.cloudsystem.application.port.out.NodeUsageScheduler;
import io.github.milesreimann.cloudsystem.application.service.NodeUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Miles R.
 * @since 07.01.26
 */
@Component
@RequiredArgsConstructor
public class SpringNodeUsageScheduler implements NodeUsageScheduler {
    private final NodeUsageService nodeUsageService;

    @Override
    @Scheduled(scheduler = "10s")
    public void schedule() {
        nodeUsageService.refreshUsages();
    }
}