package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.model.CPU;

import java.util.Objects;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
public class CPUImpl implements CPU {
    private final long millicores;

    public CPUImpl(long millicores) {
        if (millicores < 0) {
            throw new IllegalArgumentException("CPU cannot be negative");
        }

        this.millicores = millicores;
    }

    public static CPU empty() {
        return new CPUImpl(0L);
    }

    @Override
    public long getMillicores() {
        return millicores;
    }

    @Override
    public double toCores() {
        return millicores / 1000D;
    }

    @Override
    public String getSuffix() {
        return "m";
    }

    @Override
    public CPU add(CPU other) {
        if (other == null) {
            return this;
        }

        return new CPUImpl(millicores + other.getMillicores());
    }

    @Override
    public CPU subtract(CPU other) {
        if (other == null) {
            return this;
        }

        long remaining = millicores - other.getMillicores();
        if (remaining < 0) {
            remaining = 0;
        }

        return new CPUImpl(remaining);
    }

    @Override
    public boolean fits(CPU other) {
        if (other == null) {
            return false;
        }

        return millicores >= other.getMillicores();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CPUImpl cpu = (CPUImpl) o;
        return millicores == cpu.millicores;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(millicores);
    }

    @Override
    public String toString() {
        return "CPUImpl{" +
            "millicores=" + millicores +
            '}';
    }
}
