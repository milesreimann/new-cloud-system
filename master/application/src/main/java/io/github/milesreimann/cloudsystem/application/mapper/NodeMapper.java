package io.github.milesreimann.cloudsystem.application.mapper;

import io.github.milesreimann.cloudsystem.api.runtime.Node;

/**
 * @author Miles R.
 * @since 07.01.26
 */
public interface NodeMapper<T> {
    Node toDomain(T node);
}
