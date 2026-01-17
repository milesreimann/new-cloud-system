package io.github.milesreimann.cloudsystem.application.port.in.template;

import io.github.milesreimann.cloudsystem.api.entity.ServerTemplate;

import java.util.List;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public interface ListServerTemplatesUseCase {
    List<ServerTemplate> listAll();

    List<ServerTemplate> listActive();

    List<ServerTemplate> listInactive();
}