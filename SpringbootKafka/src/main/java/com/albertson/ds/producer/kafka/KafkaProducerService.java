package com.albertson.ds.producer.kafka;

import com.albertson.ds.producer.inventory.Inventory;
import com.albertson.ds.producer.item.Item;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInventory(Inventory inventory)
    {
        log.info(String.format("Message sent -> %s", inventory));
        this.kafkaTemplate.send("OSDC_ITEM_UPDATE_QA", inventory);
    }

    public void sendItemToKafka(Item item)
    {
        log.info(String.format("Message sent -> %s", item));
        this.kafkaTemplate.send("OSDC_ITEM_INSERT_QA", item);
    }





}
