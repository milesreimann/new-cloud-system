package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundle;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public interface FileLoaderPort {
    CompletableFuture<ServerFileBundle> loadServerGroupFiles(ServerGroup serverGroup);

    CompletableFuture<ServerFileBundle> loadServerTemplateFiles(ServerTemplate serverTemplate);
}