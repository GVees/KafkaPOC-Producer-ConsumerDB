package com.albertson.ds.producer.common;

import com.albertson.ds.producer.inventory.Inventory;
import com.albertson.ds.producer.item.Item;
import com.albertson.ds.producer.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.util.List;


@Component
@Getter
@Slf4j
public class JsonReadForInventory extends  AbstractJsonRead {

    @Async
    @PostConstruct
    public void launchInventoryMonitoring() {
        log.info("START_MONITORING_INVENTORY");
        try {
            WatchKey key;
            while ((key = watchServiceForInventory.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path filePath = (Path) event.context();
                    log.info("Event kind: {}; File affected: {}", event.kind(), event.context());
                    if (!event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                        String filePathToProcess = key.watchable().toString() + "\\" + filePath.getFileName();
                        log.info(filePathToProcess);
                        try {
                            final List<Inventory> inventories = readInventoryFromJson(filePathToProcess);
                            inventories.forEach(inventory -> kafkaProducerService.sendInventory(inventory));
                            moveFilesToProcessedFolder(new File(filePathToProcess), commonProperties.getInventoryArchiveFolderPath());
                            break;
                        } catch (IOException e) {
                            log.error("IO exception occured: {}", e.getMessage());
                        }
                    }

                }
                key.reset();
            }
        } catch (InterruptedException e) {
            log.warn("interrupted exception for monitoring service");
        }
    }

    @Async
    @PostConstruct
    public void launchItemMonitoring() {
        log.info("START_MONITORING_Item");
        try {
            WatchKey key;
            while ((key = watchServiceForItem.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path filePath = (Path) event.context();
                    log.info("Event kind: {}; File affected: {}", event.kind(), event.context());
                    if (!event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                        String filePathToProcess = key.watchable().toString() + "\\" + filePath.getFileName();
                        log.info(filePathToProcess);
                        try {
                            final List<Item> items = readItemFromJson(filePathToProcess);
                            items.forEach(item -> kafkaProducerService.sendItemToKafka(item));
                            moveFilesToProcessedFolder(new File(filePathToProcess), commonProperties.getItemArchiveFolderPath());
                            break;
                        } catch (IOException e) {
                            log.error("IO exception occured: {}", e.getMessage());
                        }
                    }

                }
                key.reset();
            }
        } catch (InterruptedException e) {
            log.warn("interrupted exception for monitoring service");
        }
    }

    @PreDestroy
    public void stopMonitoringInventory() {
        log.info("STOP_MONITORING Inventory");

        if (watchServiceForInventory != null) {
            try {
                watchServiceForInventory.close();
            } catch (IOException e) {
                log.error("exception while closing the monitoring service");
            }
        }
    }

    @PreDestroy
    public void stopMonitoringItem() {
        log.info("STOP_MONITORING Item");

        if (watchServiceForItem!= null) {
            try {
                watchServiceForItem.close();
            } catch (IOException e) {
                log.error("exception while closing the monitoring service");
            }
        }
    }
}
