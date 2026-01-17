package io.github.milesreimann.cloudsystem.adapter.framework.spring.config;

import io.github.milesreimann.cloudsystem.application.port.out.FileArchiverPort;
import io.github.milesreimann.cloudsystem.application.port.out.FileDeploymentPort;
import io.github.milesreimann.cloudsystem.application.port.out.FileLoaderPort;
import io.github.milesreimann.cloudsystem.adapter.archiving.targz.out.TarGzFileArchiver;
import io.github.milesreimann.cloudsystem.adapter.storage.filesystem.out.FileSystemFileDeployment;
import io.github.milesreimann.cloudsystem.adapter.storage.filesystem.out.FileSystemFileLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.Executor;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
@Configuration
public class FileConfig {
    // TODO: Remove hardcoded paths

    private static final Path BASE_PATH = Path.of("C:\\Users\\deroq\\Desktop\\cloud-system-test");
    private static final Path PUBLISH_ROOT = BASE_PATH;
    private static final URI PUBLIC_BASE_URI = URI.create("http://localhost:8080/files/");

    @Bean
    public FileArchiverPort fileArchiverPort() {
        return new TarGzFileArchiver();
    }

    @Bean
    public FileLoaderPort fileLoaderPort(Executor executor) {
        return new FileSystemFileLoader(BASE_PATH, executor);
    }

    @Bean
    public FileDeploymentPort fileDeploymentPort(FileArchiverPort fileArchiverPort) {
        return new FileSystemFileDeployment(
            PUBLISH_ROOT,
            PUBLIC_BASE_URI,
            fileArchiverPort
        );
    }
}