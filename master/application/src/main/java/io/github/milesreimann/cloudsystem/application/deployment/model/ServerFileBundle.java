package io.github.milesreimann.cloudsystem.application.deployment.model;

import java.util.Map;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
public record ServerFileBundle(Map<String, ServerFile> files) {
    public static ServerFileBundle empty() {
        return new ServerFileBundle(Map.of());
    }
}