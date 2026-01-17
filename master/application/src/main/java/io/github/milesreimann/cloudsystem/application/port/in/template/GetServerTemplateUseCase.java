package io.github.milesreimann.cloudsystem.application.port.in.template;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;

import java.util.Optional;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public interface GetServerTemplateUseCase {
    Optional<ServerTemplate> getById(long templateId);
}