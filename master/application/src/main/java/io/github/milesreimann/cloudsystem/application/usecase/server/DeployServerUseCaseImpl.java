package io.github.milesreimann.cloudsystem.application.usecase.server;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.deployment.FilesBundler;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.port.in.server.DeployServerUseCase;
import io.github.milesreimann.cloudsystem.application.port.out.FileDeploymentPort;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class DeployServerUseCaseImpl implements DeployServerUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DeployServerUseCaseImpl.class);

    private final FilesBundler filesBundler;
    private final FileDeploymentPort fileDeploymentPort;
    private final ServerDeploymentPort serverDeploymentPort;

    public DeployServerUseCaseImpl(
        FilesBundler filesBundler,
        FileDeploymentPort fileDeploymentPort,
        ServerDeploymentPort serverDeploymentPort
    ) {
        this.filesBundler = filesBundler;
        this.fileDeploymentPort = fileDeploymentPort;
        this.serverDeploymentPort = serverDeploymentPort;
    }

    @Override
    public CompletableFuture<Void> deploy(Server server) {
        ServerTemplate template = server.getTemplate();

        LOG.debug("Starting deployment for server '{}' (template: '{}')",
            server.getName(), template.getName());

        return filesBundler.bundleFilesForServer(template)
            .thenCompose(bundle -> deployFilesAndServer(server, bundle))
            .whenComplete((_, error) -> {
                if (error != null) {
                    LOG.error("Deployment failed for server '{}'", server.getName(), error);
                } else {
                    LOG.info("Deployment completed for server '{}'", server.getName());
                }
            });
    }

    private CompletableFuture<Void> deployFilesAndServer(Server server, ServerFileBundle bundle) {
        return fileDeploymentPort.deployFiles(server, bundle)
            .thenCompose(deployment -> serverDeploymentPort.deployServer(server, deployment));
    }
}