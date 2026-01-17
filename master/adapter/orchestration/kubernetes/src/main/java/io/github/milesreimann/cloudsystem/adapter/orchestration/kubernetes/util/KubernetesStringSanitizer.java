package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.util;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Miles R.
 * @since 10.01.2026
 */
public class KubernetesStringSanitizer {
    private KubernetesStringSanitizer() {
    }

    public static String sanitize(String string) {
        return Objects.requireNonNull(string)
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9-]", "-")
            .replaceAll("^[^a-z0-9]+", "")
            .replaceAll("[^a-z0-9]+$", "");
    }
}