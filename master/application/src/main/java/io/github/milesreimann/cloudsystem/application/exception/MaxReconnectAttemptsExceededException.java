package io.github.milesreimann.cloudsystem.application.exception;

/**
 * @author Miles R.
 * @since 07.01.26
 */
public class MaxReconnectAttemptsExceededException extends RuntimeException {
    public MaxReconnectAttemptsExceededException(String message) {
        super(message);
    }
}
