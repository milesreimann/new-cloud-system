package io.github.milesreimann.cloudsystem.api.model;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public interface Resources {
    double getCpu();

    Memory getMemory();

    Resources add(Resources other);

    Resources subtract(Resources other);

    boolean fits(Resources other);
}