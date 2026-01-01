package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.application.cache.ServerCache;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class ServerService {
    private final ServerCache serverCache;

    public ServerService(ServerCache serverCache) {
        this.serverCache = serverCache;
    }

    public long getServerCountForTemplate(long templateId) {
        return serverCache.values().stream()
            .filter(server -> server.getTemplateId() == templateId)
            .count();
    }
}
