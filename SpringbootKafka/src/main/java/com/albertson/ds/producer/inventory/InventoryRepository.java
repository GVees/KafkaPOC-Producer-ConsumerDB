package com.albertson.ds.producer.inventory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InventoryRepository extends MongoRepository<Inventory,String> {
}
