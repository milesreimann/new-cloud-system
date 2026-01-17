package io.github.milesreimann.cloudsystem.adapter.spring.config;

import io.github.milesreimann.cloudsystem.application.port.out.FileArchiverPort;
import io.github.milesreimann.cloudsystem.application.port.out.FileDeploymentPort;
import io.github.milesreimann.cloudsystem.application.port.out.FileLoaderPort;
import io.github.milesreimann.cloudsystem.filesystem.FileSystemFileDeployment;
import io.github.milesreimann.cloudsystem.filesystem.FileSystemFileLoader;
import io.github.milesreimann.cloudsystem.filesystem.ZipFileArchiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.nio.file.Path;

/**
 * @author Miles R.
 * @since 17.01.2026
 */
@Configuration
public class FileConfig {
    // Wo liegen groups/ und templates/ als Ordner?
    private static final Path BASE_PATH = Path.of("C:\\Users\\deroq\\Desktop\\cloud-system-test");

    // Wo sollen gepackte Artefakte landen (wird von HTTP served)?
    private static final Path PUBLISH_ROOT = BASE_PATH; // z.B. gleicher Root, schreibt nach BASE_PATH/servers/...

    // Unter welcher URL ist PUBLISH_ROOT erreichbar?
    // Beispiel: du servest C:\...\cloud-system-test als http://localhost:8080/files/
    private static final URI PUBLIC_BASE_URI = URI.create("http://localhost:8080/files/");

    @Bean
    public FileArchiverPort fileArchiverPort() {
        return new ZipFileArchiver();
    }

    @Bean
    public FileLoaderPort fileLoaderPort() {
        return new FileSystemFileLoader(BASE_PATH);
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
