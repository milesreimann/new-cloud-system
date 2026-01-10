package io.github.milesreimann.cloudsystem.master.domain.entity;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
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
    private final ServerTemplate template;
    private final ServerStatus status;
    private final Instant startedAt;

    private ServerImpl(
        UUID uniqueId,
        long id,
        ServerTemplate template,
        ServerStatus status,
        Instant startedAt
    ) {
        this.uniqueId = uniqueId;
        this.id = id;
        this.template = template;
        this.status = status;
        this.startedAt = startedAt;
    }

    public static Server create(long id, ServerTemplate template) {
        return new ServerImpl(
            UUID.randomUUID(),
            id,
            template,
            ServerStatus.CREATING,
            Instant.now()
        );
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return template.getGroup().getAbbreviation() + "-" + getId();
    }

    @Override
    public ServerTemplate getTemplate() {
        return template;
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