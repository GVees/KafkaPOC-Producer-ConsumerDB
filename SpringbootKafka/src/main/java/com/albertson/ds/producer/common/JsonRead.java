package com.albertson.ds.producer.common;

import com.albertson.ds.producer.item.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.albertson.ds.producer.inventory.Inventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;


@Service
@Getter
@Slf4j
public class JsonRead {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ResourceLoader resourceLoader;
    public List<Item> readItemFromJson() throws IOException
    {
        Resource resource = resourceLoader.getResource("file:C:/TestData/itemfile.json");
                return(List<Item>)readPayload(resource, new TypeReference<List< Item>>() {
       });
    }

    public List<Inventory> readInventoryFromJson() throws IOException
    {
        Resource resource = resourceLoader.getResource("file:C:/TestData/InventoryFarDS.json");

        return(List<Inventory>)readPayload(resource, new TypeReference<List< Inventory>>() {
        });
    }

    private Object readPayload(Resource jsonFile, TypeReference typeReference) throws IOException{
        Object object = objectMapper.readValue(jsonFile.getFile(), typeReference);
        log.info("Object read from json ", object);
        moveFilesToProcessedFolder(jsonFile.getFile());
        return object;
    }

    private void moveFilesToProcessedFolder(File fileToBeMove) throws IOException{
        Files.move(fileToBeMove.toPath(), Paths.get("C:/TestData/archive/"+fileToBeMove.getName()));
    }
}
