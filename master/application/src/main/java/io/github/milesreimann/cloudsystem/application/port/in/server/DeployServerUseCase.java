package io.github.milesreimann.cloudsystem.application.port.in.server;

import io.github.milesreimann.cloudsystem.api.entity.Server;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public interface DeployServerUseCase {
    CompletableFuture<Void> deploy(Server server);
}