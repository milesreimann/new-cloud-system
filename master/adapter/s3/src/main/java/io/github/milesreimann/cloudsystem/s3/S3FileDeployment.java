package io.github.milesreimann.cloudsystem.s3;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.application.model.ArchiveFormat;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundleDeployment;
import io.github.milesreimann.cloudsystem.application.port.out.FileArchiverPort;
import io.github.milesreimann.cloudsystem.application.port.out.FileDeploymentPort;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class S3FileDeployment implements FileDeploymentPort {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;
    private final String prefix;
    private final FileArchiverPort archiver;

    public S3FileDeployment(
        S3Client s3Client,
        S3Presigner s3Presigner,
        String bucketName,
        String prefix,
        FileArchiverPort archiver
    ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
        this.prefix = prefix;
        this.archiver = archiver;
    }

    @Override
    public CompletableFuture<ServerFileBundleDeployment> deployFiles(Server server, ServerFileBundle bundle) {
        return archiver.archive(bundle)
            .thenApply(archived -> {
                ArchiveFormat archiveFormat = archived.format();
                String key = prefix + "/servers/" + server.getUniqueId().toString() + "." + archiveFormat.getExtension();

                s3Client.putObject(
                    PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(archiveFormat.getContentType())
                        .build(),
                    RequestBody.fromBytes(archived.bytes())
                );

                GetObjectRequest getReq = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

                GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .getObjectRequest(getReq)
                    .build();

                URI url;
                try {
                    url = s3Presigner.presignGetObject(presignReq).url().toURI();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

                return new ServerFileBundleDeployment(url, archiveFormat, key);
            });
    }

    @Override
    public CompletableFuture<Void> deleteFiles(Server server) {
        return CompletableFuture.failedFuture(new UnsupportedOperationException("Not implemented yet."));
    }
}