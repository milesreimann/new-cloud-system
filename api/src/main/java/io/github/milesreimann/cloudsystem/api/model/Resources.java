package io.github.milesreimann.cloudsystem.api.model;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public interface Resources extends MeasurableResource<Resources> {
    CPU getCpu();

    Memory getMemory();
}