package io.github.milesreimann.cloudsystem.master.domain.entity;

import io.github.milesreimann.cloudsystem.api.entity.Server;
import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;
import io.github.milesreimann.cloudsystem.api.model.ServerStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Miles R.
 * @since 08.01.2026
 */
public class ServerImpl implements Server {
    private final UUID uniqueId;
    private final long id;
    private final String nodeName;
    private final ServerTemplate template;
    private final ServerStatus status;
    private final Instant startedAt;

    private ServerImpl(
        UUID uniqueId,
        long id,
        String nodeName,
        ServerTemplate template,
        ServerStatus status,
        Instant startedAt
    ) {
        this.uniqueId = Objects.requireNonNull(uniqueId, "uniqueId cannot be null");
        this.id = id;
        this.nodeName = Objects.requireNonNull(nodeName, "nodeName cannot be null");
        this.template = Objects.requireNonNull(template, "template cannot be null");
        this.status = Objects.requireNonNull(status, "status cannot be null");
        this.startedAt = Objects.requireNonNull(startedAt, "startedAt cannot be null");
    }

    public static Server create(long id, String nodeName, ServerTemplate template) {
        return new ServerImpl(
            UUID.randomUUID(),
            id,
            nodeName,
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
    public String getNodeName() {
        return nodeName;
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