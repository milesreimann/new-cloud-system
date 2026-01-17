package io.github.milesreimann.cloudsystem.adapter.storage.filesystem.out;

import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.deployment.model.FileSource;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFile;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.port.out.FileLoaderPort;
import io.github.milesreimann.cloudsystem.adapter.storage.filesystem.exception.ServerFileBundleLoadException;
import io.github.milesreimann.cloudsystem.adapter.storage.filesystem.exception.ServerFileReadException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class FileSystemFileLoader implements FileLoaderPort {
    private static final String GROUPS_PATH = "groups";
    private static final String TEMPLATES_PATH = "templates";

    private final Path basePath;
    private final Executor executor;

    public FileSystemFileLoader(Path basePath, Executor executor) {
        this.basePath = basePath;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<ServerFileBundle> loadServerGroupFiles(ServerGroup serverGroup) {
        return CompletableFuture.supplyAsync(
            () -> {
                Path groupPath = basePath.resolve(GROUPS_PATH).resolve(serverGroup.getAbbreviation());
                return loadFilesFromDirectory(groupPath, FileSource.SERVER_GROUP);
            },
            executor
        );
    }

    @Override
    public CompletableFuture<ServerFileBundle> loadServerTemplateFiles(ServerTemplate serverTemplate) {
        return CompletableFuture.supplyAsync(
            () -> {
                Path templatePath = basePath.resolve(TEMPLATES_PATH).resolve(serverTemplate.getAbbreviation());
                return loadFilesFromDirectory(templatePath, FileSource.SERVER_TEMPLATE);
            },
            executor
        );
    }

    private ServerFileBundle loadFilesFromDirectory(Path directory, FileSource source) {
        if (!Files.exists(directory)) {
            return ServerFileBundle.empty();
        }

        Map<String, ServerFile> files = new HashMap<>();

        try (Stream<Path> paths = Files.walk(directory)) {
            paths.filter(Files::isRegularFile)
                .map(path -> readServerFile(directory, path, source))
                .forEach(serverFile -> files.put(serverFile.relativePath(), serverFile));
        } catch (IOException e) {
            throw new ServerFileBundleLoadException("Failed to load server files from directory: " + directory, e);
        }

        return new ServerFileBundle(files);
    }

    private ServerFile readServerFile(Path directory, Path file, FileSource fileSource) {
        try {
            String relativePath = directory.relativize(file)
                .toString()
                .replace('\\', '/');

            byte[] content = Files.readAllBytes(file);

            return new ServerFile(relativePath, content, fileSource);
        } catch (IOException e) {
            throw new ServerFileReadException("Failed to read server file: " + file, e);
        }
    }
}