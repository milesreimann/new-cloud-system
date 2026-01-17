package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.application.model.ArchivedServerFiles;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundle;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public interface FileArchiverPort {
    CompletableFuture<ArchivedServerFiles> archive(ServerFileBundle bundle);
}