package io.github.milesreimann.cloudsystem.master.adapter.persistence.repository;

import io.github.milesreimann.cloudsystem.master.adapter.persistence.entity.JpaServerTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 29.12.2025
 */
public interface JpaServerTemplateRepository extends JpaRepository<JpaServerTemplate, Long> {
    Optional<JpaServerTemplate> findByAbbreviation(String abbreviation);

    @Query("""
        SELECT st
        FROM JpaServerTemplate st
        WHERE (:active IS NULL OR st.active = :active)
    """)
    List<JpaServerTemplate> findByActive(@Param("active") Boolean active);
}
