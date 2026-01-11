package io.github.milesreimann.cloudsystem.adapter.spring.persistence.entity;

import io.github.milesreimann.cloudsystem.api.model.DeploymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author Miles R.
 * @since 10.01.2026
 */
@Entity
@Table(name = "server_deployment_metadata")
@Data
public class JpaDeploymentMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "server_template_id", insertable = false, updatable = false)
    private JpaServerTemplate serverTemplate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeploymentType deploymentType;

    @Column(name = "metadata_key", nullable = false)
    private String key;

    @Column(name = "metadata_value", nullable = false)
    private String value;
}
