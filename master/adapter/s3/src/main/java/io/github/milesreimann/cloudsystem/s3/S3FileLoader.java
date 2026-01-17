package io.github.milesreimann.cloudsystem.s3;

import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.model.FileSource;
import io.github.milesreimann.cloudsystem.application.model.ServerFile;
import io.github.milesreimann.cloudsystem.application.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.port.out.FileLoaderPort;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class S3FileLoader implements FileLoaderPort {
    private static final String GROUPS_PATH = "groups";
    private static final String TEMPLATES_PATH = "templates";

    private final S3Client s3Client;
    private final String bucketName;
    private final String basePrefix;

    public S3FileLoader(S3Client s3Client, String bucketName, String basePrefix) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.basePrefix = basePrefix;
    }

    @Override
    public CompletableFuture<ServerFileBundle> loadServerGroupFiles(ServerGroup serverGroup) {
        return CompletableFuture.supplyAsync(() -> {
            String prefix = String.format("%s/%s/%s/", basePrefix, GROUPS_PATH, serverGroup.getAbbreviation());
            return loadFilesFromS3(prefix, FileSource.SERVER_GROUP);
        });
    }

    @Override
    public CompletableFuture<ServerFileBundle> loadServerTemplateFiles(ServerTemplate serverTemplate) {
        return CompletableFuture.supplyAsync(() -> {
            String prefix = String.format("%s/%s/%s/", basePrefix, TEMPLATES_PATH, serverTemplate.getAbbreviation());
            return loadFilesFromS3(prefix, FileSource.SERVER_TEMPLATE);
        });
    }

    private ServerFileBundle loadFilesFromS3(String prefix, FileSource source) {
        Map<String, ServerFile> files = new HashMap<>();

        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
            .bucket(bucketName)
            .prefix(prefix)
            .build();

        s3Client.listObjectsV2(listRequest)
            .contents()
            .stream()
            .filter(s3Object -> !s3Object.key().endsWith("/"))
            .map(s3Object -> loadFileFromS3(s3Object.key(), prefix, source))
            .forEach(serverFile -> files.put(serverFile.relativePath(), serverFile));

        return new ServerFileBundle(files);
    }

    private ServerFile loadFileFromS3(String key, String prefix, FileSource source) {
        String relativePath = key.substring(prefix.length());
        byte[] content = downloadFile(key);

        return new ServerFile(relativePath, content, source);
    }

    private byte[] downloadFile(String key) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getRequest);
        return responseBytes.asByteArray();
    }
}
