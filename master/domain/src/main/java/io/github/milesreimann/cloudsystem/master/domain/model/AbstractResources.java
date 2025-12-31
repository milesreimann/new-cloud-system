package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.model.Resources;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public abstract class AbstractResources implements Resources {
    protected abstract Resources createNew(double cpu, double memory);

    @Override
    public Resources add(Resources other) {
        if (other == null) {
            return this;
        }

        return createNew(
            getCpu() + other.getCpu(),
            getMemory() + other.getMemory()
        );
    }

    @Override
    public Resources subtract(Resources other) {
        if (other == null) {
            return this;
        }

        return createNew(
            getCpu() - other.getCpu(),
            getMemory() - other.getMemory()
        );
    }

    @Override
    public boolean fits(Resources required) {
        if (required == null) {
            return false;
        }

        return getCpu() >= required.getCpu()
            && getMemory() >= required.getMemory();
    }
}
