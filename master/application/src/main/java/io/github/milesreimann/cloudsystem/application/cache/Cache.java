package io.github.milesreimann.cloudsystem.application.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Miles R.
 * @since 31.12.2025
 */
public abstract class Cache<K, V> {
    private final Map<K, V> cache = new ConcurrentHashMap<>();

    public V put(K key, V value) {
        return cache.put(key, value);
    }

    public boolean remove(K key) {
        return cache.remove(key) != null;
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(cache.get(key));
    }

    public Collection<V> values() {
        return cache.values();
    }

    public int size() {
        return cache.size();
    }
}
