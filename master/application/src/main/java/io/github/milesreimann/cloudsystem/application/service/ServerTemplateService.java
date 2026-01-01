package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.cache.ServerTemplateCache;
import io.github.milesreimann.cloudsystem.application.port.out.ServerTemplateRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class ServerTemplateService {
    private final ServerTemplateRepository serverTemplateRepository;
    private final ServerTemplateCache serverTemplateCache;

    public ServerTemplateService(ServerTemplateRepository serverTemplateRepository, ServerTemplateCache serverTemplateCache) {
        this.serverTemplateRepository = serverTemplateRepository;
        this.serverTemplateCache = serverTemplateCache;

        warmUpCache();
    }

    public List<ServerTemplate> listServerTemplates(Boolean active) {
        Collection<ServerTemplate> groups = serverTemplateCache.values();

        if (active == null) {
            return new ArrayList<>(groups);
        }

        return groups.stream()
            .filter(group -> group.isActive() == active)
            .toList();
    }

    private void warmUpCache() {
        serverTemplateRepository.findAll().forEach(group -> serverTemplateCache.put(group.getId(), group));
    }
}