package com.albertson.ds.producer.common;

import com.albertson.ds.producer.inventory.Inventory;
import com.albertson.ds.producer.item.Item;
import com.albertson.ds.producer.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
abstract class AbstractJsonRead {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    CommonProperties commonProperties;
    @Autowired
    KafkaProducerService kafkaProducerService;
    @Autowired
     WatchService watchServiceForInventory;
    @Autowired
     WatchService watchServiceForItem;

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

    protected Object readPayload(File jsonFile, TypeReference typeReference) throws IOException {
        log.info("json file: {}", jsonFile);
        Object object = objectMapper.readValue(jsonFile, typeReference);
        log.info("Object read from json {}", object);
        return object;
    }

    protected void moveFilesToProcessedFolder(File fileToBeMove, String newLocationPath) throws IOException {
        Files.move(fileToBeMove.toPath(), Paths.get(newLocationPath + fileToBeMove.getName() + LocalDateTime.now()), StandardCopyOption.REPLACE_EXISTING);
    }

}
