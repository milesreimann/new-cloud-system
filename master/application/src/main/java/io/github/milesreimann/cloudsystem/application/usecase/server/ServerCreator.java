package io.github.milesreimann.cloudsystem.application.usecase.server;

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
 * @since 17.01.2026
 */
public class ServerCreator {
    private final ServerCache serverCache;
    private final Map<Long, Object> templateLocks = new ConcurrentHashMap<>();

    public ServerCreator(ServerCache serverCache) {
        this.serverCache = serverCache;
    }

    public synchronized Server createServer(String nodeName, ServerTemplate template) {
        Object lock = templateLocks.computeIfAbsent(template.getId(), _ -> new Object());

        synchronized (lock) {
            long id = generateUniqueServerId(template.getGroup());
            Server server = ServerImpl.create(id, nodeName, template);
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

    public int countServersForTemplate(long templateId) {
        Object lock = templateLocks.computeIfAbsent(templateId, _ -> new Object());

        synchronized (lock) {
            return (int) serverCache.values().stream()
                .filter(server -> server.getTemplate().getId() == templateId)
                .count();
        }
    }

    private long generateUniqueServerId(ServerGroup serverGroup) {
        List<Long> existingIds = serverCache.values().stream()
            .filter(server -> server.getTemplate().getId().equals(serverGroup.getId()))
            .map(Server::getId)
            .sorted()
            .toList();

        long nextId = 1;
        for (long id : existingIds) {
            if (id == nextId) {
                nextId++;
            } else if (id > nextId) {
                break;
            }
        }

        return nextId;
    }
}