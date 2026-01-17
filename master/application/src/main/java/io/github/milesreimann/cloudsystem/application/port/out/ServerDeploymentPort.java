package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFileBundleDeployment;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 07.01.26
 */
public interface ServerDeploymentPort {
    CompletableFuture<Void> deployServer(Server server, ServerFileBundleDeployment bundleDeployment);
}
