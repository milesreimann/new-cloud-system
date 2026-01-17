package io.github.milesreimann.cloudsystem.adapter.storage.filesystem.exception;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class ServerFileReadException extends RuntimeException {
    public ServerFileReadException(String message, Exception e) {
        super(message, e);
    }
}