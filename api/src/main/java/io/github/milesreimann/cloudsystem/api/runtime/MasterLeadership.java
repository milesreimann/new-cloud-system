package io.github.milesreimann.cloudsystem.api.runtime;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public interface MasterLeadership {
    // Future

    boolean isLeader();

    void start();

    void stop();
}
