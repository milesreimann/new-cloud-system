package io.github.milesreimann.cloudsystem.application.exception;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
public class ServerGroupAlreadyExistsException extends RuntimeException {
    public ServerGroupAlreadyExistsException(String message) {
        super(message);
    }
}