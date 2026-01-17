package io.github.milesreimann.cloudsystem.filesystem;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.application.model.ArchiveFormat;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundleDeployment;
import io.github.milesreimann.cloudsystem.application.port.out.FileArchiverPort;
import io.github.milesreimann.cloudsystem.application.port.out.FileDeploymentPort;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class FileSystemFileDeployment implements FileDeploymentPort {
    private final Path publishRoot;
    private final URI publicBaseUri;
    private final FileArchiverPort archiver;

    public FileSystemFileDeployment(
        Path publishRoot,
        URI publicBaseUri,
        FileArchiverPort archiver
    ) {
        this.publishRoot = publishRoot;
        this.publicBaseUri = publicBaseUri;
        this.archiver = archiver;
    }

    @Override
    public CompletableFuture<ServerFileBundleDeployment> deployFiles(Server server, ServerFileBundle bundle) {
        return archiver.archive(bundle)
            .thenApply(archived -> {
                ArchiveFormat archiveFormat = archived.format();
                String fileName = server.getUniqueId() + "." + archiveFormat.getExtension();
                Path targetDir = publishRoot.resolve("servers");
                Path targetFile = targetDir.resolve(fileName);

                try {
                    Files.createDirectories(targetDir);
                    Files.write(targetFile, archived.bytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to write server bundle artifact: " + targetFile, e);
                }

                URI url = publicBaseUri.resolve("servers/" + fileName);
                return new ServerFileBundleDeployment(url, archiveFormat, targetFile.toString());
            });
    }

    @Override
    public CompletableFuture<Void> deleteFiles(Server server) {
        return CompletableFuture.failedFuture(new UnsupportedOperationException("Not implemented yet."));
    }
}