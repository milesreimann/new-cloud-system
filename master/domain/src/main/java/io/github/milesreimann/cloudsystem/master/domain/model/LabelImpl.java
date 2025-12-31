package io.github.milesreimann.cloudsystem.master.domain.model;

import io.github.milesreimann.cloudsystem.api.model.Label;

/**
 * @author Miles R.
 * @since 26.12.2025
 */
public class LabelImpl implements Label {
    private final String key;
    private final String value;

    private LabelImpl(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static Label of(String key, String value) {
        return new LabelImpl(key, value);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
