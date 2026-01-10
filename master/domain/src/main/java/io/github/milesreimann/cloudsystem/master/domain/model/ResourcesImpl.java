package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.Objects;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public class ResourcesImpl extends AbstractResources {
    private final double cpu;
    private final Memory memory;

    public ResourcesImpl(double cpu, Memory memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    public static Resources empty() {
        return new ResourcesImpl(0D, MemoryImpl.empty());
    }

    @Override
    protected Resources createNew(double cpu, Memory memory) {
        return new ResourcesImpl(cpu, memory);
    }

    @Override
    public double getCpu() {
        return cpu;
    }

    @Override
    public Memory getMemory() {
        return memory;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResourcesImpl resources = (ResourcesImpl) o;
        return Double.compare(cpu, resources.cpu) == 0 && Objects.equals(memory, resources.memory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpu, memory);
    }

    @Override
    public String toString() {
        return "ResourcesImpl{" +
            "cpu=" + cpu +
            ", memory=" + memory +
            '}';
    }
}
