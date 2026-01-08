package io.github.milesreimann.cloudsystem.adapter.spring.out;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.port.out.ServerTemplateRepository;
import io.github.milesreimann.cloudsystem.adapter.spring.mapper.ServerTemplateMapper;
import io.github.milesreimann.cloudsystem.adapter.spring.persistence.repository.JpaServerTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
@Repository
@RequiredArgsConstructor
public class ServerTemplateRepositoryAdapter implements ServerTemplateRepository {
    private final JpaServerTemplateRepository jpaRepository;
    private final ServerTemplateMapper serverTemplateMapper;

    @Override
    public List<ServerTemplate> findAll() {
        return jpaRepository.findAll().stream()
            .map(serverTemplateMapper::toDomain)
            .toList();
    }
}
