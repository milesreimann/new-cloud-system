package io.github.milesreimann.cloudsystem.adapter.framework.spring.config;

import io.github.milesreimann.cloudsystem.application.deployment.FilesBundler;
import io.github.milesreimann.cloudsystem.application.port.out.FileLoaderPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
@Configuration
public class DeploymentConfig {
    @Bean
    public FilesBundler filesBundler(FileLoaderPort fileLoaderPort) {
        return new FilesBundler(fileLoaderPort);
    }
}