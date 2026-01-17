package io.github.milesreimann.cloudsystem.adapter.framework.spring.scheduling;

import io.github.milesreimann.cloudsystem.application.port.in.monitoring.RefreshNodeUsageUseCase;
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
    private final RefreshNodeUsageUseCase refreshNodeUsageUseCase;

    @Scheduled(fixedDelayString = "10s")
    public void schedule() {
        refreshNodeUsageUseCase.refreshAll();
    }
}