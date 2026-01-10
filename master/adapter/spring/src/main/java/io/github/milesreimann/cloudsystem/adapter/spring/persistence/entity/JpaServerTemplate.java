package io.github.milesreimann.cloudsystem.adapter.spring.persistence.entity;

import io.github.milesreimann.cloudsystem.api.entity.DeploymentMetadata;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.adapter.spring.persistence.model.EmbeddedResources;
import io.github.milesreimann.cloudsystem.api.model.Resources;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
@Entity
@Table(name = "server_templates")
@Data
public class JpaServerTemplate implements ServerTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 9, unique = true, nullable = false)
    private String abbreviation;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private JpaServerGroup group;

    @Column(name = "min_servers", nullable = false)
    private int minServers;

    @Column(name = "max_servers")
    private Integer maxServers;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "cpu", column = @Column(name = "requirements_cpu")),
        @AttributeOverride(name = "memory.value", column = @Column(name = "requirements_memory")),
        @AttributeOverride(name = "memory.unit", column = @Column(name = "requirements_memory_unit"))
    })
    private EmbeddedResources requirements;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "cpu", column = @Column(name = "limits_cpu")),
        @AttributeOverride(name = "memory.value", column = @Column(name = "limits_memory")),
        @AttributeOverride(name = "memory.unit", column = @Column(name = "limits_memory_unit"))
    })
    private EmbeddedResources limits;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "serverTemplate", cascade = CascadeType.ALL)
    private List<JpaDeploymentMetadata> deploymentMetadata;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "server_template_envs", joinColumns = @JoinColumn(name = "server_template_id"))
    @MapKeyColumn(name = "env_key")
    @Column(name = "env_value")
    private Map<String, String> environmentVariables;

    @Column(nullable = false)
    private boolean active;

    @Override
    public Optional<Resources> getLimits() {
        return Optional.ofNullable(limits);
    }

    @Override
    public List<DeploymentMetadata> getDeploymentMetadata() {
        return deploymentMetadata.stream()
            .map(metadata -> (DeploymentMetadata) metadata)
            .toList();
    }
}