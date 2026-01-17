package io.github.milesreimann.cloudsystem.application.deployment.model;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public record ArchivedServerFiles(
    byte[] bytes,
    ArchiveFormat format
) {
}