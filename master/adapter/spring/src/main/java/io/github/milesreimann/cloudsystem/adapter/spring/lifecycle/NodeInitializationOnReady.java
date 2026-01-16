package io.github.milesreimann.cloudsystem.adapter.spring.lifecycle;

import io.github.milesreimann.cloudsystem.application.service.NodeInitializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Component
@RequiredArgsConstructor
public class NodeInitializationOnReady {
    private final NodeInitializationService nodeInitializationService;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        nodeInitializationService.initialize();
    }
}