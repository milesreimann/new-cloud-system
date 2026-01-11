package io.github.milesreimann.cloudsystem.master.domain.entity;

import io.github.milesreimann.cloudsystem.api.entity.DeploymentMetadata;
import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
public class ServerTemplateImpl implements ServerTemplate {
    private final Long id;
    private final String abbreviation;
    private final String name;
    private final ServerGroup group;
    private final int minServers;
    private final Integer maxServers;
    private final Resources requirements;
    private final Resources limits;
    private final List<DeploymentMetadata> deploymentMetadata;
    private final Map<String, String> environmentVariables;
    private final boolean active;

    public ServerTemplateImpl(
        Long id,
        String abbreviation,
        String name,
        ServerGroup group,
        int minServers,
        Integer maxServers,
        Resources requirements,
        Resources limits,
        List<DeploymentMetadata> deploymentMetadata,
        Map<String, String> environmentVariables,
        boolean active
    ) {
        this.id = id;
        this.abbreviation = Objects.requireNonNull(abbreviation, "abbreviation cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.group = Objects.requireNonNull(group, "group cannot be null");
        this.minServers = minServers;
        this.maxServers = maxServers;
        this.requirements = Objects.requireNonNull(requirements, "requirements cannot be null");
        this.limits = limits;
        this.deploymentMetadata = deploymentMetadata;
        this.environmentVariables = environmentVariables;
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
    public ServerGroup getGroup() {
        return group;
    }

    @Override
    public int getMinServers() {
        return minServers;
    }

    @Override
    public Integer getMaxServers() {
        return maxServers;
    }

    @Override
    public Resources getRequirements() {
        return requirements;
    }

    @Override
    public Optional<Resources> getLimits() {
        return Optional.ofNullable(limits);
    }

    @Override
    public List<DeploymentMetadata> getDeploymentMetadata() {
        return deploymentMetadata;
    }

    @Override
    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServerTemplateImpl that = (ServerTemplateImpl) o;
        return minServers == that.minServers && active == that.active && Objects.equals(id, that.id) && Objects.equals(abbreviation, that.abbreviation) && Objects.equals(name, that.name) && Objects.equals(group, that.group) && Objects.equals(maxServers, that.maxServers) && Objects.equals(requirements, that.requirements) && Objects.equals(limits, that.limits) && Objects.equals(deploymentMetadata, that.deploymentMetadata) && Objects.equals(environmentVariables, that.environmentVariables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, abbreviation, name, group, minServers, maxServers, requirements, limits, deploymentMetadata, environmentVariables, active);
    }

    @Override
    public String toString() {
        return "ServerTemplateImpl{" +
            "id=" + id +
            ", abbreviation='" + abbreviation + '\'' +
            ", name='" + name + '\'' +
            ", group=" + group +
            ", minServers=" + minServers +
            ", maxServers=" + maxServers +
            ", requirements=" + requirements +
            ", limits=" + limits +
            ", deploymentMetadata=" + deploymentMetadata +
            ", environmentVariables=" + environmentVariables +
            ", active=" + active +
            '}';
    }
}
