package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;

import java.util.List;

/**
 * @author Miles R.
 * @since 01.01.2026
 */
public interface ServerTemplateRepository {
    List<ServerTemplate> findAll();
}
