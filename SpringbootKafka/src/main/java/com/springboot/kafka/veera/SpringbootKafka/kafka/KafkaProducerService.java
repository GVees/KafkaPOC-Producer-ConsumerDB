package com.springboot.kafka.veera.SpringbootKafka.kafka;

import com.springboot.kafka.veera.SpringbootKafka.inventory.Inventory;
import com.springboot.kafka.veera.SpringbootKafka.item.Item;
import com.springboot.kafka.veera.SpringbootKafka.item.JsonRead;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaProducerService {

    @Autowired
    private JsonRead jsonRead;

    @Autowired
    private KafkaProducer<String,Item> defaultKafkaProducer;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInventory(Inventory inventory)
    {
        log.info(String.format("Message sent -> %s", inventory));
        this.kafkaTemplate.send("OSDC_ITEM_UPDATE_QA", inventory);
    }
    public void readFile(){
        log.info(""+jsonRead.getItems());
        log.info("Testing");
    }

    public void produceMessageToKafka(){

        List<Item> items = jsonRead.getItems();
        items.stream().forEach(item -> {
            defaultKafkaProducer.send(new ProducerRecord<String,Item>("ItemTopic",item));
        });
        log.info("Producer has sent all Item records successfully...");
       // defaultKafkaProducer.close();
        
    }


}
