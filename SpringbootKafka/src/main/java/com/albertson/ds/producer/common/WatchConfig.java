package com.albertson.ds.producer.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;

@Slf4j
@Configuration
public class WatchConfig {

    @Value("${scanning-folder}")
    private String inventoryFolderPath;

    @Bean
    public WatchService watchServiceForInventory() {
        log.debug("Inventory scanning_FOLDER: {}", inventoryFolderPath);
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(inventoryFolderPath);

            if (!Files.isDirectory(path)) {
                throw new RuntimeException("incorrect monitoring folder: " + path);
            }

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE
            );
        } catch (IOException e) {
            log.error("exception for watch service creation:", e);
        }
        return watchService;
    }

}
