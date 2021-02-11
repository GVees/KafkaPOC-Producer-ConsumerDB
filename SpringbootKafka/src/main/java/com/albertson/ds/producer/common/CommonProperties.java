package com.albertson.ds.producer.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Getter
public class CommonProperties {
    @Value("${scanning.inventory.folder.path}")
    private String inventoryFolderPath;

    @Value("${scanning.item.folder.path}")
    private String itemFolderPath;

    @Value("${scanning.item.archive.folder.path}")
    private String itemArchiveFolderPath;

    @Value("${scanning.inventory.archive.folder.path}")
    private String inventoryArchiveFolderPath;

    @Value("${inventory.file.path}")
    private String inventoryFilePath;

    @Value("${item.file.path}")
    private String itemFilePath;
}
