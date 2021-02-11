package com.albertson.ds.producer.item;

import com.albertson.ds.producer.common.CommonProperties;
import com.albertson.ds.producer.common.JsonReadForInventory;
import com.albertson.ds.producer.kafka.KafkaProducerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    private JsonReadForInventory jsonReadForInventory;
    private KafkaProducerService kafkaProducerService;
    private CommonProperties commonProperties;


    public void readItemAndSendToKafka() throws IOException
    {
        List<Item> items = jsonReadForInventory.readItemFromJson(commonProperties.getItemFilePath());
        items.forEach(item -> kafkaProducerService.sendItemToKafka(item));
    }
}

