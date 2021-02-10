package com.medsan.inventory.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Slf4j
public class InventoryDetail {

    private String storeId;
    private String upcId;

}
