package io.github.milesreimann.cloudsystem.api.model;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
public interface CPU extends MeasurableResource<CPU> {
    long getMillicores();

    double toCores();

    String getSuffix();
}
