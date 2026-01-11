package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.model.CPU;
import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.Objects;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public class ResourcesImpl extends AbstractResources {
    private final CPU cpu;
    private final Memory memory;

    public ResourcesImpl(CPU cpu, Memory memory) {
        this.cpu = Objects.requireNonNull(cpu, "CPU cannot be null");
        this.memory = Objects.requireNonNull(memory, "Memory cannot be null");
    }

    public static Resources empty() {
        return new ResourcesImpl(CPUImpl.empty(), MemoryImpl.empty());
    }

    @Override
    protected Resources createNew(CPU cpu, Memory memory) {
        return new ResourcesImpl(cpu, memory);
    }

    @Override
    public CPU getCpu() {
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
        return Objects.equals(cpu, resources.cpu) && Objects.equals(memory, resources.memory);
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
