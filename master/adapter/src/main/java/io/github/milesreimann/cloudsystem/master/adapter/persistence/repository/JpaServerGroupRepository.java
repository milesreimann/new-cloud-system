package io.github.milesreimann.cloudsystem.master.adapter.persistence.repository;

import io.github.milesreimann.cloudsystem.master.adapter.persistence.entity.JpaServerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 28.12.2025
 */
@Repository
public interface JpaServerGroupRepository extends JpaRepository<JpaServerGroup, Long> {
    Optional<JpaServerGroup> findByAbbreviation(String abbreviation);

    @Query("""
        SELECT sg
        FROM JpaServerGroup sg
        WHERE (:active IS NULL OR sg.active = :active)
    """)
    List<JpaServerGroup> findByActive(@Param("active") Boolean active);
}
