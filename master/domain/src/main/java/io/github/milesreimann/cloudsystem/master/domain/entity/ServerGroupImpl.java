package io.github.milesreimann.cloudsystem.master.domain.entity;

import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.api.model.ServerGroupType;

import java.util.Objects;
import java.util.Set;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
public class ServerGroupImpl implements ServerGroup {
    private final Long id;
    private final String abbreviation;
    private final String name;
    private final ServerGroupType type;
    private final Set<String> requiredNodeLabels;
    private final Set<String> preferredNodeLabels;
    private final boolean active;

    public ServerGroupImpl(
        Long id,
        String abbreviation,
        String name,
        ServerGroupType type,
        Set<String> requiredNodeLabels,
        Set<String> preferredNodeLabels,
        boolean active
    ) {
        this.id = id;
        this.abbreviation = Objects.requireNonNull(abbreviation, "Abbreviation cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.requiredNodeLabels = requiredNodeLabels;
        this.preferredNodeLabels = preferredNodeLabels;
        this.active = active;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ServerGroupType getType() {
        return type;
    }

    @Override
    public Set<String> getPreferredNodeLabels() {
        return preferredNodeLabels;
    }

    @Override
    public Set<String> getRequiredNodeLabels() {
        return requiredNodeLabels;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isStatic() {
        return type == ServerGroupType.STATIC;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServerGroupImpl that = (ServerGroupImpl) o;
        return active == that.active && Objects.equals(id, that.id) && Objects.equals(abbreviation, that.abbreviation) && Objects.equals(name, that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, abbreviation, name, type, requiredNodeLabels, preferredNodeLabels, active);
    }

    @Override
    public String toString() {
        return "ServerGroupImpl{" +
            "id=" + id +
            ", abbreviation='" + abbreviation + '\'' +
            ", name='" + name + '\'' +
            ", type=" + type +
            ", requiredNodeLabels=" + requiredNodeLabels +
            ", preferredNodeLabels=" + preferredNodeLabels +
            ", active=" + active +
            '}';
    }
}