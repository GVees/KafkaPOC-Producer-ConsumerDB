package com.albertson.ds.producer.kafka;

import com.albertson.ds.producer.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

@Configuration
@ConfigurationProperties
@Slf4j
public class KafkaConfig {

    @Value("${application.kafka.client.id}")
    private String clientId;

    @Value("${application.kafka.bootstrap.server}")
    private String kafkaServer;

}
