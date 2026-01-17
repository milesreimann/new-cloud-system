package io.github.milesreimann.cloudsystem.application.deployment;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFile;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFileBundle;
import io.github.milesreimann.cloudsystem.application.port.out.FileLoaderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class FilesBundler {
    private static final Logger LOG = LoggerFactory.getLogger(FilesBundler.class);

    private final FileLoaderPort fileLoaderPort;

    public FilesBundler(FileLoaderPort fileLoaderPort) {
        this.fileLoaderPort = fileLoaderPort;
    }

    public CompletableFuture<ServerFileBundle> bundleFilesForServer(ServerTemplate template) {
        LOG.debug("Bundling files for template '{}'", template.getName());

        CompletableFuture<ServerFileBundle> groupFilesFuture = fileLoaderPort.loadServerGroupFiles(template.getGroup());
        CompletableFuture<ServerFileBundle> templateFilesFuture = fileLoaderPort.loadServerTemplateFiles(template);

        return groupFilesFuture
            .thenCombine(templateFilesFuture, (groupFiles, templateFiles) -> {
                ServerFileBundle mergedFiles = mergeFiles(groupFiles, templateFiles);
                LOG.debug("Bundled {} files for template '{}'", mergedFiles.files().size(), template.getName());

                return mergedFiles;
            });
    }

    private ServerFileBundle mergeFiles(ServerFileBundle groupFiles, ServerFileBundle templateFiles) {
        Map<String, ServerFile> merged = new HashMap<>(groupFiles.files());
        merged.putAll(templateFiles.files());

        return new ServerFileBundle(merged);
    }
}