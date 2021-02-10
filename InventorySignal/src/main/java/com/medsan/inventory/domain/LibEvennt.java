package com.medsan.inventory.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
@Builder
public class LibEvennt {

    private Integer libraryEventId;
    private InventoryDetail inventoryDetail;


}
