package io.github.milesreimann.cloudsystem.application.port.out;

import java.io.Closeable;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public interface NodeWatcherPort extends Closeable {
    void watch();
}