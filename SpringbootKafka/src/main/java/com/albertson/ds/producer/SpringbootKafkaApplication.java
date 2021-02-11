package com.albertson.ds.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableAsync
public class SpringbootKafkaApplication {


	@Bean
	public TaskExecutor threadPoolTaskExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(5);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("Monitor-");
		return executor;
	}

	public static void main(String[] args) {

		SpringApplication.run(SpringbootKafkaApplication.class, args);



	}
}
