package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.exception;

/**
 * @author Miles R.
 * @since 10.01.2026
 */
public class MissingImageException extends RuntimeException {
    public MissingImageException(String message) {
        super(message);
    }
}
