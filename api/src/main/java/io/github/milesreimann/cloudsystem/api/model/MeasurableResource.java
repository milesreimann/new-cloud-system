package io.github.milesreimann.cloudsystem.api.model;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
public interface MeasurableResource<T extends MeasurableResource<T>> {
    T add(T other);

    T subtract(T other);

    boolean fits(T other);
}