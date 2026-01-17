package io.github.milesreimann.cloudsystem.application.deployment.model;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public enum ArchiveFormat {
    TAR_GZ("tgz", "application/gzip"),
    ZIP("zip", "application/zip");

    private final String extension;
    private final String contentType;

    ArchiveFormat(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }
}