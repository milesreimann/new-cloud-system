package io.github.milesreimann.cloudsystem.application.usecase.template;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.cache.ServerTemplateCache;
import io.github.milesreimann.cloudsystem.application.port.in.template.GetServerTemplateUseCase;
import io.github.milesreimann.cloudsystem.application.port.in.template.ListServerTemplatesUseCase;
import io.github.milesreimann.cloudsystem.application.port.out.ServerTemplateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public class ServerTemplateQueryService implements GetServerTemplateUseCase, ListServerTemplatesUseCase {
    private final ServerTemplateCache cache;

    public ServerTemplateQueryService(ServerTemplateRepository repository, ServerTemplateCache cache) {
        this.cache = cache;
        warmUpCache(repository);
    }

    @Override
    public Optional<ServerTemplate> getById(long templateId) {
        return cache.get(templateId);
    }

    @Override
    public List<ServerTemplate> listAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public List<ServerTemplate> listActive() {
        return filterByActiveStatus(true);
    }

    @Override
    public List<ServerTemplate> listInactive() {
        return filterByActiveStatus(false);
    }

    private List<ServerTemplate> filterByActiveStatus(boolean active) {
        return cache.values().stream()
            .filter(template -> template.isActive() == active)
            .toList();
    }

    private void warmUpCache(ServerTemplateRepository repository) {
        repository.findAll().forEach(template -> cache.put(template.getId(), template));
    }
}