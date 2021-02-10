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


@Service
@Getter
@Slf4j
public class JsonRead {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    CommonProperties commonProperties;
    @Autowired
    KafkaProducerService kafkaProducerService;
    @Autowired
    private WatchService watchServiceForInventory;
    @Autowired
    private WatchService watchServiceForItem;

    public List<Item> readItemFromJson() throws IOException {
        Resource resource = resourceLoader.getResource("file:C:/TestData/itemfile.json");
        return (List<Item>) readPayload(resource.getFile(), new TypeReference<List<Item>>() {
        });
    }

    public List<Item> readItemFromJson(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource("file:"+ filePath);
        return (List<Item>) readPayload(resource.getFile(), new TypeReference<List<Item>>() {
        });
    }

    public List<Inventory> readInventoryFromJson() throws IOException {
        return readInventoryFromJson("C:/TestData/InventoryFarDS.json");
    }

    public List<Inventory> readInventoryFromJson(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource("file:" + filePath);
        return (List<Inventory>) readPayload(new File(filePath), new TypeReference<List<Inventory>>() {
        });
    }

    private Object readPayload(File jsonFile, TypeReference typeReference) throws IOException {
        log.info("json file: {}", jsonFile);
        Object object = objectMapper.readValue(jsonFile, typeReference);
        log.info("Object read from json {}", object);
        return object;
    }

    private void moveFilesToProcessedFolder(File fileToBeMove, String newLocationPath) throws IOException {
        Files.move(fileToBeMove.toPath(), Paths.get(newLocationPath + fileToBeMove.getName() + LocalDateTime.now()), StandardCopyOption.REPLACE_EXISTING);
    }

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
        log.info("START_MONITORING_INVENTORY");
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
    public void stopMonitoring() {
        log.info("STOP_MONITORING");

        if (watchServiceForInventory != null) {
            try {
                watchServiceForInventory.close();
            } catch (IOException e) {
                log.error("exception while closing the monitoring service");
            }
        }
    }
}
