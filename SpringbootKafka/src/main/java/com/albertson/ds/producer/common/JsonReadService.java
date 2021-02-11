package com.albertson.ds.producer.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class JsonReadService {

    @Autowired
    JsonReadForInventory jsonReadForInventory;

    @Autowired
    JsonReadForItem jsonReadForItem;

    @PostConstruct
    public void startJsonRead(){
        jsonReadForInventory.launchInventoryMonitoring();
        jsonReadForItem.launchItemMonitoring();
    }

}
