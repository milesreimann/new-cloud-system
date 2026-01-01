package io.github.milesreimann.cloudsystem.master.adapter.scheduling;

import io.github.milesreimann.cloudsystem.application.service.ServerSchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
@Component
@RequiredArgsConstructor
public class ServerScheduler {
    private final ServerSchedulingService serverSchedulingService;

    @Scheduled(fixedDelayString = "${server.scheduling.period}")
    public void tick() {
        serverSchedulingService.schedule();
    }
}
