package com.springboot.kafka.veera.SpringbootKafka.kafka;

import com.springboot.kafka.veera.SpringbootKafka.inventory.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
@AllArgsConstructor
public class KafkaResource {

    private KafkaProducerService kafkaProducerService;
    private InventoryService inventoryService;

    @PostMapping(value = "/produce-item")
    @ResponseStatus(code = HttpStatus.OK)
    public void produceItemToKafka(){

        kafkaProducerService.produceMessageToKafka();
        inventoryService.readAndSendInvenotryToTopic();
    }

}
