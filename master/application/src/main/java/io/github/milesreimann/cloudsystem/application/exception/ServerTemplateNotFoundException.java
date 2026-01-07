package io.github.milesreimann.cloudsystem.application.exception;

/**
 * @author Miles R.
 * @since 07.01.26
 */
public class ServerTemplateNotFoundException extends RuntimeException {
    public ServerTemplateNotFoundException(long id) {
        super("ServerTemplate with id=" + id + " was not found");
    }
}
