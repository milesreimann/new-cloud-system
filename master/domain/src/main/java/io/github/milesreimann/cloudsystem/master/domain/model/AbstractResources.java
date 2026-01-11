package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.model.CPU;
import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.api.model.Resources;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public abstract class AbstractResources implements Resources {
    protected abstract Resources createNew(CPU cpu, Memory memory);

    @Override
    public Resources add(Resources other) {
        if (other == null) {
            return this;
        }

        CPU combinedCpu = getCpu().add(other.getCpu());
        Memory combinedMemory = getMemory().add(other.getMemory());

        return createNew(combinedCpu, combinedMemory);
    }

    @Override
    public Resources subtract(Resources other) {
        if (other == null) {
            return this;
        }

        CPU remainingCpu = getCpu().subtract(other.getCpu());
        Memory remainingMemory = getMemory().subtract(other.getMemory());

        return createNew(remainingCpu, remainingMemory);
    }

    @Override
    public boolean fits(Resources required) {
        if (required == null) {
            return false;
        }

        return getCpu().fits(required.getCpu()) && getMemory().fits(required.getMemory());
    }
}