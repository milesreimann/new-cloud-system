package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundleDeployment;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public interface FileDeploymentPort {
    CompletableFuture<ServerFileBundleDeployment> deployFiles(Server server, ServerFileBundle bundle);

    CompletableFuture<Void> deleteFiles(Server server);
}