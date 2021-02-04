package com.springboot.kafka.veera.SpringbootKafka.kafka.config;

import com.springboot.kafka.veera.SpringbootKafka.item.Item;
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

    @Bean
    public KafkaProducer<String, Item> defaultKafkaProducer()   {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,clientId);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer .class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        log.info("Producer has been created...Start sending Item Record ");

        return new KafkaProducer<String,Item>(props);


    }
}
