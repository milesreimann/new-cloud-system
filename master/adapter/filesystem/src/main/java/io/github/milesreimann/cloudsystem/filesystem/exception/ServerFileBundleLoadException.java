package io.github.milesreimann.cloudsystem.filesystem.exception;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class ServerFileBundleLoadException extends RuntimeException {
    public ServerFileBundleLoadException(String message, Exception e) {
        super(message, e);
    }
}