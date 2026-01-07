package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.runtime.Node;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 07.01.26
 */
public interface ServerDeploymentPort {
    CompletableFuture<Server> deployServer(Node targetNode, ServerTemplate serverTemplate);
}
