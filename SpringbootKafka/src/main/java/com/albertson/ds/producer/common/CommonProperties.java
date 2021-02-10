package com.albertson.ds.producer.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Getter
public class CommonProperties {
    @Value("${scanning-folder-inventory}")
    private String inventoryFolderPath;

    @Value("${scanning-folder-item}")
    private String itemFolderPath;

    @Value("${scanning-folder-item-archive}")
    private String itemArchiveFolderPath;

    @Value("${scanning-folder-inventory-archive}")
    private String inventoryArchiveFolderPath;


}
