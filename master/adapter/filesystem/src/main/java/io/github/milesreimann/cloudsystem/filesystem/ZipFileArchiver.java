package io.github.milesreimann.cloudsystem.filesystem;

import io.github.milesreimann.cloudsystem.application.model.ArchiveFormat;
import io.github.milesreimann.cloudsystem.application.model.ArchivedServerFiles;
import io.github.milesreimann.cloudsystem.application.model.ServerFile;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.port.out.FileArchiverPort;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class ZipFileArchiver implements FileArchiverPort {
    @Override
    public CompletableFuture<ArchivedServerFiles> archive(ServerFileBundle bundle) {
        return CompletableFuture.supplyAsync(() -> {
            try (
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(byteOutputStream)
            ) {
                for (Map.Entry<String, ServerFile> entry : bundle.files().entrySet()) {
                    ZipEntry zipEntry = new ZipEntry(entry.getKey());
                    zipOutputStream.putNextEntry(zipEntry);
                    zipOutputStream.write(entry.getValue().content());
                    zipOutputStream.closeEntry();
                }

                zipOutputStream.finish();

                return new ArchivedServerFiles(byteOutputStream.toByteArray(), ArchiveFormat.ZIP);
            } catch (IOException e) {
                throw new RuntimeException("Failed to archive bundle as zip", e);
            }
        });
    }
}