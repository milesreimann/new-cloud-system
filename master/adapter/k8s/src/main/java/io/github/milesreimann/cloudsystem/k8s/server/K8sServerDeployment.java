package io.github.milesreimann.cloudsystem.k8s.server;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 08.01.2026
 */
public class K8sServerDeployment implements ServerDeploymentPort {
    @Override
    public CompletableFuture<Server> deployServer(Node targetNode, ServerTemplate serverTemplate) {
        return CompletableFuture.failedFuture(new RuntimeException("Not implemented yet"));
    }
}
