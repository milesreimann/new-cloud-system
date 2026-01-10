package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.api.model.Resources;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public abstract class AbstractResources implements Resources {
    protected abstract Resources createNew(double cpu, Memory memory);

    @Override
    public Resources add(Resources other) {
        if (other == null) {
            return this;
        }

        double combinedCpu = getCpu() + other.getCpu();
        long totalBytes = getMemory().toBytes() + other.getMemory().toBytes();
        Memory combinedMemory = MemoryImpl.fromBytes(totalBytes, getMemory().getUnit());

        return createNew(combinedCpu, combinedMemory);
    }

    @Override
    public Resources subtract(Resources other) {
        if (other == null) {
            return this;
        }

        double remainingCpu = getCpu() - other.getCpu();
        long remainingBytes = getMemory().toBytes() - other.getMemory().toBytes();
        Memory remainingMemory = MemoryImpl.fromBytes(remainingBytes, getMemory().getUnit());

        return createNew(remainingCpu, remainingMemory);
    }

    @Override
    public boolean fits(Resources required) {
        if (required == null) {
            return false;
        }

        return getCpu() >= required.getCpu()
            && getMemory().toBytes() >= required.getMemory().toBytes();
    }
}
