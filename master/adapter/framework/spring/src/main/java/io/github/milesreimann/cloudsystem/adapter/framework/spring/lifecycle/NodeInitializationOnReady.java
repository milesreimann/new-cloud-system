package io.github.milesreimann.cloudsystem.adapter.framework.spring.lifecycle;

import io.github.milesreimann.cloudsystem.application.port.in.node.InitializeNodesUseCase;
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
    private final InitializeNodesUseCase initializeNodesUseCase;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        initializeNodesUseCase.initialize();
    }
}