package io.github.milesreimann.cloudsystem.api.model;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public enum ServerStatus {
    CREATING,
    STARTING,
    RUNNING,
    STOPPING,
    STOPPED,
    CRASHED,
    FAILED;
}
