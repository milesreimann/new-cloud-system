package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.pod;

import io.github.milesreimann.cloudsystem.application.deployment.model.ArchiveFormat;
import io.github.milesreimann.cloudsystem.application.deployment.model.ServerFileBundleDeployment;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class InitContainerSpec {
    private static final String IMAGE = "alpine:latest";
    public static final String DATA_MOUNT_PATH = "/data";

    public String getImageForFormat(ArchiveFormat format) {
        return switch (format) {
            case TAR_GZ, ZIP -> IMAGE;
        };
    }

    public String buildDownloadAndExtractCommand(ServerFileBundleDeployment deployment) {
        String url = deployment.uri().toString();

        return switch (deployment.format()) {
            case TAR_GZ -> buildTarGzCommand(url);
            case ZIP -> buildZipCommand(url);
        };
    }

    private String buildTarGzCommand(String url) {
        return String.format(
            "set -e; " +
                "mkdir -p %s; " +
                "wget -qO /tmp/bundle.tgz \"%s\"; " +
                "tar -xzf /tmp/bundle.tgz -C %s;",
            DATA_MOUNT_PATH, url, DATA_MOUNT_PATH
        );
    }

    private String buildZipCommand(String url) {
        return String.format(
            "set -e; " +
                "mkdir -p %s; " +
                "wget -qO /tmp/bundle.zip \"%s\"; " +
                "unzip -oq /tmp/bundle.zip -d %s;",
            DATA_MOUNT_PATH, url, DATA_MOUNT_PATH
        );
    }
}