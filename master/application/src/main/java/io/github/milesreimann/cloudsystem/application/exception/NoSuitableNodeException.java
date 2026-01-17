package io.github.milesreimann.cloudsystem.application.exception;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;

/**
 * @author Miles R.
 * @since 07.01.26
 */
public class NoSuitableNodeException extends RuntimeException {
    public NoSuitableNodeException(ServerTemplate serverTemplate) {
        super("No suitable node available for template '" + serverTemplate.getName());
    }
}