package io.github.milesreimann.cloudsystem.application.port.out;

import io.github.milesreimann.cloudsystem.api.model.Resources;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public interface NodeUsageProvider {
    Resources getUsage(String nodeName);
}
