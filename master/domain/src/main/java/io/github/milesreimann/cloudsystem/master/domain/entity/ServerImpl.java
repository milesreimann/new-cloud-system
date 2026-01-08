package io.github.milesreimann.cloudsystem.master.domain.entity;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.model.ServerStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Miles R.
 * @since 08.01.2026
 */
public class ServerImpl implements Server {
    private final UUID uniqueId;
    private final long id;
    //private final String nodeId;
    private final long templateId;
    private final ServerStatus status;
    private final Instant startedAt;

    public ServerImpl(
        UUID uniqueId,
        long id,
        //String nodeId,
        long templateId,
        ServerStatus status,
        Instant startedAt
    ) {
        this.uniqueId = uniqueId;
        this.id = id;
        //this.nodeId = nodeId;
        this.templateId = templateId;
        this.status = status;
        this.startedAt = startedAt;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public long getId() {
        return id;
    }

    /*@Override
    public String getNodeId() {
        return nodeId;
    }
     */

    @Override
    public long getTemplateId() {
        return templateId;
    }

    @Override
    public ServerStatus getStatus() {
        return status;
    }

    @Override
    public Instant getStartedAt() {
        return startedAt;
    }
}
