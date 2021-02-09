package com.albertson.ds.producer.inventory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document (collection = "InventoryFAR")
@Getter
@ToString
public class Inventory {
    @Id
    private String storeId;

    private String upcId;
    private List<InventoryDetail> inventoryDetails;

    @JsonCreator
    public static Inventory of(@JsonProperty(value = "storeId") final String storeId,
                               @JsonProperty(value = "upcId") final String upcId,
                               @JsonProperty(value = "inventoryDetails") final List<InventoryDetail> inventoryDetails){
        return new Inventory(storeId,upcId, inventoryDetails);
    }

 }
