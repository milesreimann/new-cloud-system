package io.github.milesreimann.cloudsystem.adapter.archiving.targz.out;

import io.github.milesreimann.cloudsystem.application.deployment.model.ArchiveFormat;
import io.github.milesreimann.cloudsystem.application.deployment.model.ArchivedServerFiles;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.port.out.FileArchiverPort;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class TarGzFileArchiver implements FileArchiverPort {
    @Override
    public CompletableFuture<ArchivedServerFiles> archive(ServerFileBundle bundle) {
        return CompletableFuture.supplyAsync(() -> {
            try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                 GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteOutputStream);
                 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(gzipOutputStream)) {

                tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);

                for (var entry : bundle.files().entrySet()) {
                    String path = entry.getKey();
                    byte[] content = entry.getValue().content();

                    TarArchiveEntry archiveEntry = new TarArchiveEntry(path);
                    archiveEntry.setSize(content.length);

                    tarOutputStream.putArchiveEntry(archiveEntry);
                    tarOutputStream.write(content);
                    tarOutputStream.closeArchiveEntry();
                }

                tarOutputStream.finish();
                return new ArchivedServerFiles(byteOutputStream.toByteArray(), ArchiveFormat.TAR_GZ);
            } catch (IOException e) {
                throw new RuntimeException("Failed to archive bundle as tar.gz", e);
            }
        });
    }
}