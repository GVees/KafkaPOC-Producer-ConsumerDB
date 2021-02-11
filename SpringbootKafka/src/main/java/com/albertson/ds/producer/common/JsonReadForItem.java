package com.albertson.ds.producer.common;

import com.albertson.ds.producer.inventory.Inventory;
import com.albertson.ds.producer.item.Item;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;


@Component
@Getter
@Slf4j
public class JsonReadForItem extends  AbstractJsonRead {

    @Async("threadPoolTaskExecutor")
    public void launchItemMonitoring() {
        log.info("START_MONITORING_Item");
        try {
            WatchKey key;
            while ((key = watchServiceForItem.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path filePath = (Path) event.context();
                    log.info("Event kind: {}; File affected: {}", event.kind(), event.context());
                    if (!event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                        String filePathToProcess = key.watchable().toString() + "\\" + filePath.getFileName();
                        log.info("File to process{} and doest it exit {}",filePathToProcess, Files.exists(Paths.get(filePathToProcess)));
                        try {
                            if(Files.exists(Paths.get(filePathToProcess))) {
                                final List<Item> items = readItemFromJson(filePathToProcess);
                                items.forEach(item -> kafkaProducerService.sendItemToKafka(item));
                                moveFilesToProcessedFolder(new File(filePathToProcess), commonProperties.getItemArchiveFolderPath());
                            }
                        } catch (IOException e) {
                            log.error("IO exception occured: {}", e.getMessage());
                        }
                    }
                }
                key.reset();
            }
        } catch (InterruptedException e) {
            log.warn("interrupted exception for monitoring service");
        }
    }


    @PreDestroy
    public void stopMonitoringItem() {
        log.info("STOP_MONITORING Item");

        if (watchServiceForItem!= null) {
            try {
                watchServiceForItem.close();
            } catch (IOException e) {
                log.error("exception while closing the monitoring service");
            }
        }
    }
}
