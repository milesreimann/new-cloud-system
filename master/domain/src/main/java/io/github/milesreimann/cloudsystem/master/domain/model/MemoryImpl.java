package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.model.Memory;
import io.github.milesreimann.cloudsystem.api.model.MemoryUnit;

import java.util.Objects;

/**
 * @author Miles R.
 * @since 10.01.2026
 */
public class MemoryImpl implements Memory {
    private final long value;
    private final MemoryUnit unit;

    public MemoryImpl(long value, MemoryUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public static Memory fromBytes(long bytes, MemoryUnit unit) {
        return new MemoryImpl(unit.fromBytes(bytes), unit);
    }

    public static Memory empty() {
        return new MemoryImpl(0L, MemoryUnit.B);
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public long toBytes() {
        return unit.toBytes(value);
    }

    @Override
    public MemoryUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemoryImpl memory = (MemoryImpl) o;
        return value == memory.value && unit == memory.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return "MemoryImpl{" +
            "value=" + value +
            ", unit=" + unit +
            '}';
    }
}