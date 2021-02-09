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
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
@Getter
@Slf4j
public class JsonRead {

    @Autowired
    ObjectMapper objectMapper;

    @Value("classpath:mockdata/itemfile.json")
    Resource itemFileJson;
    //@Value("classpath:mockdata/InventoryFarDS.json")
    //Resource inventoryFarDsResource;
    @Autowired
    ResourceLoader resourceLoader;
    public List<Item> readItemFromJson() throws IOException
    {
        return(List<Item>)readPayload(itemFileJson, new TypeReference<List< Item>>() {
        });
    }
    public List<Inventory> readInventoryFromJson() throws IOException
    {
        Resource resource = resourceLoader.getResource("classpath:mockdata/InventoryFarDS.json");

        return(List<Inventory>)readPayload(resource, new TypeReference<List< Inventory>>() {
        });
    }

    public Object readPayload(Resource jsonFile, TypeReference typeReference) throws IOException{
        Object object = objectMapper.readValue(jsonFile.getInputStream(), typeReference);
        log.info("Object read from json ", object);
        return object;
    }
}
