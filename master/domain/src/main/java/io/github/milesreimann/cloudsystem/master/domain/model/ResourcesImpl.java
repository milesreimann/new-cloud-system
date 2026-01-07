package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.Objects;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public class ResourcesImpl extends AbstractResources {
    private final double cpu;
    private final double memory;

    public ResourcesImpl(double cpu, double memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    @Override
    protected Resources createNew(double cpu, double memory) {
        return new ResourcesImpl(cpu, memory);
    }

    public static Resources empty() {
        return new ResourcesImpl(0D, 0D);
    }

    public double getMemory() {
        return memory;
    }

    public double getCpu() {
        return cpu;
    }

    @Override
    public String toString() {
        return "ResourcesImpl{" +
            "cpu=" + cpu +
            ", memory=" + memory +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResourcesImpl resources = (ResourcesImpl) o;
        return Double.compare(cpu, resources.cpu) == 0 && Double.compare(memory, resources.memory) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpu, memory);
    }
}
