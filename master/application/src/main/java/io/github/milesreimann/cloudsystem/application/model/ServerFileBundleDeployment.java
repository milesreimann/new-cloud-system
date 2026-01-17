package io.github.milesreimann.cloudsystem.application.model;

import java.net.URI;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public record ServerFileBundleDeployment(
    URI uri,
    ArchiveFormat format,
    String relativePath
) {
}
