package io.github.milesreimann.cloudsystem.application.model;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public record ServerFile(
    String relativePath,
    byte[] content,
    FileSource source
) {
}
