package com.springboot.kafka.veera.SpringbootKafka.inventory;

import com.springboot.kafka.veera.SpringbootKafka.kafka.KafkaProducerService;
import com.springboot.kafka.veera.SpringbootKafka.item.JsonRead;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryService {

    private JsonRead jsonRead;
    private KafkaProducerService kafkaProducerService;

    public void readAndSendInvenotryToTopic(){
        List<Inventory> inventories = jsonRead.getInventories();
        inventories.forEach(inventory -> kafkaProducerService.sendInventory(inventory));
    }

}
