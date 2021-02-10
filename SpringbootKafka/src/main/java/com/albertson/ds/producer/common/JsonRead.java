package com.albertson.ds.producer.common;

import com.albertson.ds.producer.item.Item;
import com.albertson.ds.producer.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.albertson.ds.producer.inventory.Inventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
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
import java.util.Collections;
import java.util.List;


@Service
@Getter
@Slf4j
public class JsonRead {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private  WatchService watchServiceForInventory;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    KafkaProducerService kafkaProducerService;

    public List<Item> readItemFromJson() throws IOException
    {
        Resource resource = resourceLoader.getResource("file:C:/TestData/itemfile.json");
                return(List<Item>)readPayload(resource.getFile(), new TypeReference<List< Item>>() {
       });
    }

    public List<Inventory> readInventoryFromJson() throws IOException
    {
        return readInventoryFromJson("C:/TestData/InventoryFarDS.json");
    }

    public List<Inventory> readInventoryFromJson(String filePath) throws IOException
    {
        Resource resource = resourceLoader.getResource("file:"+filePath);
        return(List<Inventory>)readPayload(new File(filePath), new TypeReference<List< Inventory>>() {
        });
    }

    private Object readPayload(File jsonFile, TypeReference typeReference) throws IOException{
        log.info("json file: {}", jsonFile);
        Object object = objectMapper.readValue(jsonFile, typeReference);
        log.info("Object read from json {}", object);
        return object;
    }

    private void moveFilesToProcessedFolder(File fileToBeMove) throws IOException{
     Files.move(fileToBeMove.toPath(), Paths.get("C:/TestData/archive/"+fileToBeMove.getName()), StandardCopyOption.REPLACE_EXISTING);
    }

    @Async
    @PostConstruct
    public void launchMonitoring() {
        log.info("START_MONITORING");
        try {
            WatchKey key;
            while ((key = watchServiceForInventory.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path filePath = (Path) event.context();
                    log.info("Event kind: {}; File affected: {}", event.kind(), event.context());

                    if(!event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                        String filePathToProcess = key.watchable().toString() + "\\" + filePath.getFileName();
                        if(Files.exists(Paths.get(filePathToProcess))) {
                            log.info(filePathToProcess);
                            final List<Inventory> inventories;
                            try {
                                inventories = readInventoryFromJson(filePathToProcess);

                            inventories.forEach(inventory -> kafkaProducerService.sendInventory(inventory));
                            moveFilesToProcessedFolder(new File(filePathToProcess));
                            break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
