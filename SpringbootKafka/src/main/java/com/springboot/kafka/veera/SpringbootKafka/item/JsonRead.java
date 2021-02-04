package com.springboot.kafka.veera.SpringbootKafka.item;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.kafka.veera.SpringbootKafka.inventory.Inventory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;


@Component
@Getter
@Slf4j
public class JsonRead {
    private List<Item> items;
    private List<Inventory> inventories;
    ObjectMapper objectMapper;

    public JsonRead(ObjectMapper mapper,@Value("classpath:mockdata/itemfile.json") Resource itemFileJson,
                    @Value("classpath:mockdata/InventoryFarDS.json") Resource inventoryFarDsResource
    ) throws IOException {
        this.objectMapper =mapper;
         items = mapper.readValue(itemFileJson.getInputStream(), new TypeReference<List< Item>>() {
        });
         inventories =  mapper.readValue(inventoryFarDsResource.getInputStream(), new TypeReference<List< Inventory>>() {
        });
         log.info("inside json read" );
    }

    public Object readPayload(Resource jsonFile, TypeReference typeReference) throws IOException{
        Object object = objectMapper.readValue(jsonFile.getInputStream(), typeReference);
        log.info("Object read from json ", object);
        return object;
    }
}
