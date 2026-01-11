package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
public interface ServerArtifactStorePort {
    CompletableFuture<Path> prepareServerWorkspace(Server server);

    CompletableFuture<Void> persistServerWorkspace(Server server);

    CompletableFuture<Void> cleanupServerWorkspace(Server server);

    CompletableFuture<Void> importServerGroup(ServerGroup serverGroup, Path source);

    CompletableFuture<Void> importServerTemplate(ServerTemplate serverTemplate, Path source);
}
