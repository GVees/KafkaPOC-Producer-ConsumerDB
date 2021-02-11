package com.albertson.ds.producer.inventory;

import com.albertson.ds.producer.common.CommonProperties;
import com.albertson.ds.producer.common.JsonReadForInventory;
import com.albertson.ds.producer.kafka.KafkaProducerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class InventoryService {

    private JsonReadForInventory jsonReadForInventory;
    private KafkaProducerService kafkaProducerService;
    private CommonProperties commonProperties;

    public void readAndSendInvenotryToTopic() throws IOException {
        List<Inventory> inventories = jsonReadForInventory.readInventoryFromJson(commonProperties.getInventoryFilePath());
        inventories.forEach(inventory -> kafkaProducerService.sendInventory(inventory));
    }

}
