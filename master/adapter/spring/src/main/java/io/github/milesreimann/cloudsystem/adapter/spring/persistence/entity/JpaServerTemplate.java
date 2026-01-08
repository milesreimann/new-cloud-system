package io.github.milesreimann.cloudsystem.adapter.spring.persistence.entity;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.adapter.spring.persistence.model.EmbeddedResources;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
    private EmbeddedResources requirements;

    @Column(nullable = false)
    private boolean active;
}