package com.albertson.ds.producer.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Document(collection = "Products")
public class Item {

    @Id
    private String id;

    private String bpnID;
    private String upcId;
    private String description;
    private String price;
    private String status;
    private String storeId;
    private String channeltype;
    private String city;
    private String zipcode;

    @JsonCreator
    public static Item of(@JsonProperty(value = "id") final String id,
                          @JsonProperty(value = "bpnId") final String bpnId,
                          @JsonProperty(value = "upcId") final String upcId,
                          @JsonProperty(value = "Description") final String description,
                          @JsonProperty(value = "Price") final String price,
                          @JsonProperty(value = "status") final String status,
                          @JsonProperty(value = "storeId") final String storeId,
                          @JsonProperty(value = "Channeltype") final String channeltype,
                          @JsonProperty(value = "City") final String city,
                          @JsonProperty(value = "zipcode") final String zipcode ) {
        return new Item(id,bpnId,upcId,description,price,status,storeId,channeltype,city, zipcode);
    }








}


