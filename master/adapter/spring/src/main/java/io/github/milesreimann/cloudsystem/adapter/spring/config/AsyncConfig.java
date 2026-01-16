package io.github.milesreimann.cloudsystem.adapter.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Configuration
public class AsyncConfig {
    @Bean(destroyMethod = "shutdown")
    public Executor nodeUsageExecutor() {
        return Executors.newFixedThreadPool(2);
    }
}