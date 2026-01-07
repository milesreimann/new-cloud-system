package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.api.runtime.Node;

/**
 * @author Miles R.
 * @since 07.01.26
 */
public interface NodeMapperPort<T> {
    Node toDomain(T node);
}
