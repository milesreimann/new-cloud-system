package io.github.milesreimann.cloudsystem.adapter.spring.schedule;

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
public class SpringNodeUsageScheduler {
    private final NodeUsageService nodeUsageService;

    @Scheduled(fixedDelayString = "10s")
    public void schedule() {
        nodeUsageService.refreshUsages();
    }
}