package io.github.milesreimann.cloudsystem.adapter.spring.config;

import io.github.milesreimann.cloudsystem.application.cache.NodeCache;
import io.github.milesreimann.cloudsystem.application.cache.ServerCache;
import io.github.milesreimann.cloudsystem.application.cache.ServerTemplateCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Configuration
public class CacheConfig {
    @Bean
    public NodeCache nodeCache() {
        return new NodeCache();
    }

    @Bean
    public ServerCache serverCache() {
        return new ServerCache();
    }

    @Bean
    public ServerTemplateCache serverTemplateCache() {
        return new ServerTemplateCache();
    }
}