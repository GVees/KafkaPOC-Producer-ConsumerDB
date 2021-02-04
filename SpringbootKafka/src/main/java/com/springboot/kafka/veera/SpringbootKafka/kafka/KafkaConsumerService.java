package com.springboot.kafka.veera.SpringbootKafka.kafka;

import com.springboot.kafka.veera.SpringbootKafka.inventory.Inventory;
import com.springboot.kafka.veera.SpringbootKafka.inventory.InventoryRepository;
import com.springboot.kafka.veera.SpringbootKafka.item.Item;
import com.springboot.kafka.veera.SpringbootKafka.item.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class KafkaConsumerService {
    private ItemRepository itemRepository;
    private InventoryRepository inventoryRepository;

    @KafkaListener(topics = "ItemTopic", groupId = "group-id")
    public void consumeItemFromTopic(Item item) {

        log.info(String.format("Message Item recieved -> %s", item));
       itemRepository.save(item);
    }

    @KafkaListener(topics = "OSDC_ITEM_UPDATE_QA", groupId = "group-id")
    public void consumeAndSaveInventory(Inventory inventory) {

        log.info(String.format("Message Inventory recieved -> %s", inventory));
        inventoryRepository.save(inventory);
    }

}

