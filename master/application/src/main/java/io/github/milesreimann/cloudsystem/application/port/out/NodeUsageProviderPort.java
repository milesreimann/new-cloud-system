package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.api.model.Resources;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public interface NodeUsageProviderPort {
    CompletableFuture<Resources> getUsage(String nodeName);
}