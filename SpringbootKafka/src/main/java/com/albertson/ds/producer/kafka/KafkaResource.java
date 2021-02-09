package com.albertson.ds.producer.kafka;

import com.albertson.ds.producer.item.ItemService;
import com.albertson.ds.producer.inventory.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/kafka")
@AllArgsConstructor
public class KafkaResource {

    private KafkaProducerService kafkaProducerService;
    private InventoryService inventoryService;
    private ItemService itemService;
    @PostMapping(value = "/produce-item")
    @ResponseStatus(code = HttpStatus.OK)
    public void produceItemToKafka() throws IOException {

        inventoryService.readAndSendInvenotryToTopic();
        itemService.readItemAndSendToKafka();
    }
}
