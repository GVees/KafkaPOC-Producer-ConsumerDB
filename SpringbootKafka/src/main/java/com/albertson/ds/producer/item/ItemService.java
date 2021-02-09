package com.albertson.ds.producer.item;

import com.albertson.ds.producer.common.JsonRead;
import com.albertson.ds.producer.kafka.KafkaProducerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    private JsonRead jsonRead;
    private KafkaProducerService kafkaProducerService;

    public void readItemAndSendToKafka() throws IOException
    {
        List<Item> items = jsonRead.readItemFromJson();
        items.forEach(item -> kafkaProducerService.sendItemToKafka(item));
    }
}

