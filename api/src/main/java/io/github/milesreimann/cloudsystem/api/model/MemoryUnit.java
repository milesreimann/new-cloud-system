package io.github.milesreimann.cloudsystem.api.model;

/**
 * @author Miles R.
 * @since 10.01.2026
 */
public enum MemoryUnit {
    B(1L, "B"),
    KI(B.toBytes(1024L), "Ki"),
    MI(KI.toBytes(1024L), "Mi"),
    GI(MI.toBytes(1024L), "Gi");

    private final long multiplier;
    private final String suffix;

    MemoryUnit(long multiplier, String suffix) {
        this.multiplier = multiplier;
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public long toBytes(long value) {
        return value * multiplier;
    }

    public long fromBytes(long bytes) {
        return bytes / multiplier;
    }
}
