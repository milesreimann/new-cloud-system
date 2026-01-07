package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.application.cache.ServerCache;

import java.util.UUID;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class ServerService {
    private final ServerCache serverCache;

    public ServerService(ServerCache serverCache) {
        this.serverCache = serverCache;
    }

    public void addServer(Server server) {
        serverCache.put(server.getUniqueId(), server);
    }

    public void removeServer(UUID uniqueId) {
        serverCache.remove(uniqueId);
    }

    public int getServerCountForTemplate(long templateId) {
        return (int) serverCache.values().stream()
            .filter(server -> server.getTemplateId() == templateId)
            .count();
    }
}