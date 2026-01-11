package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerGroup;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.application.cache.ServerCache;
import io.github.milesreimann.cloudsystem.master.domain.entity.ServerImpl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public class ServerService {
    private final ServerCache serverCache;
    private final Map<Long, Object> templateLocks = new ConcurrentHashMap<>();

    public ServerService(ServerCache serverCache) {
        this.serverCache = serverCache;
    }

    public synchronized Server addServer(ServerTemplate serverTemplate) {
        Object lock = templateLocks.computeIfAbsent(serverTemplate.getId(), _ -> new Object());

        synchronized (lock) {
            long id = generateServerId(serverTemplate.getGroup());
            Server server = ServerImpl.create(id, serverTemplate);
            serverCache.put(server.getUniqueId(), server);
            return server;
        }
    }

    public void removeServer(Server server) {
        Object lock = templateLocks.computeIfAbsent(server.getTemplate().getId(), _ -> new Object());

        synchronized (lock) {
            serverCache.remove(server.getUniqueId());
        }
    }

    public int getServerCountForTemplate(long templateId) {
        Object lock = templateLocks.computeIfAbsent(templateId, _ -> new Object());

        synchronized (lock) {
            return (int) serverCache.values().stream()
                .filter(server -> server.getTemplate().getId() == templateId)
                .count();
        }
    }

    private long generateServerId(ServerGroup serverGroup) {
        List<Long> ids = serverCache.values().stream()
            .filter(server -> server.getTemplate().getId().equals(serverGroup.getId()))
            .map(Server::getId)
            .sorted()
            .toList();

        long nextId = 1;
        for (long id : ids) {
            if (id == nextId) {
                nextId++;
                continue;
            }

            if (id > nextId) {
                break;
            }
        }

        return nextId;
    }
}