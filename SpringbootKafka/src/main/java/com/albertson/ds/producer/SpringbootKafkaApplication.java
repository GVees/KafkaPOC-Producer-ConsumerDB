package com.albertson.ds.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringbootKafkaApplication {



	public static void main(String[] args) {

		SpringApplication.run(SpringbootKafkaApplication.class, args);



	}
}
