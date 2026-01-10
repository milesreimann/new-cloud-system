package io.github.milesreimann.cloudsystem.api.model;

/**
 * @author Miles R.
 * @since 10.01.2026
 */
public interface Memory {
    long getValue();

    long toBytes();

    MemoryUnit getUnit();
}