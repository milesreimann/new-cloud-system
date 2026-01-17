package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.model.ServerFile;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.port.out.FileDeploymentPort;
import io.github.milesreimann.cloudsystem.application.port.out.FileLoaderPort;
import io.github.milesreimann.cloudsystem.application.port.out.ServerDeploymentPort;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class ServerDeploymentService {
    private final FileLoaderPort fileLoaderPort;
    private final FileDeploymentPort fileDeploymentPort;
    private final ServerDeploymentPort serverDeploymentPort;

    public ServerDeploymentService(
        FileLoaderPort fileLoaderPort,
        FileDeploymentPort fileDeploymentPort,
        ServerDeploymentPort serverDeploymentPort
    ) {
        this.fileLoaderPort = fileLoaderPort;
        this.fileDeploymentPort = fileDeploymentPort;
        this.serverDeploymentPort = serverDeploymentPort;
    }

    public CompletableFuture<Void> deploy(Server server) {
        ServerTemplate template = server.getTemplate();

        CompletableFuture<ServerFileBundle> groupFilesFuture = fileLoaderPort.loadServerGroupFiles(template.getGroup());
        CompletableFuture<ServerFileBundle> templateFilesFuture = fileLoaderPort.loadServerTemplateFiles(template);

        return groupFilesFuture
            .thenCombine(templateFilesFuture, this::mergeFiles)
            .thenCompose(serverFileBundle -> fileDeploymentPort.deployFiles(server, serverFileBundle))
            .thenCompose(bundleDeployment -> serverDeploymentPort.deployServer(server, bundleDeployment));
    }

    private ServerFileBundle mergeFiles(ServerFileBundle groupFiles, ServerFileBundle templateFiles) {
        Map<String, ServerFile> merged = new HashMap<>(groupFiles.files());
        merged.putAll(templateFiles.files());

        return new ServerFileBundle(merged);
    }
}