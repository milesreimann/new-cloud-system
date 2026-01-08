package io.github.milesreimann.cloudsystem.adapter.spring.persistence.entity;

import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.api.model.ServerGroupType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Set;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
@Entity
@Table(name = "server_groups")
@Data
public class JpaServerGroup implements ServerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 9, unique = true, nullable = false)
    private String abbreviation;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServerGroupType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "server_group_required_labels", joinColumns = @JoinColumn(name = "server_group_id"))
    @Column(name = "label")
    private Set<String> requiredNodeLabels;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "server_group_required_labels", joinColumns = @JoinColumn(name = "server_group_id"))
    @Column(name = "label")
    private Set<String> preferredNodeLabels;

    @Column(nullable = false)
    private boolean active;
}
