package com.foodDelivery.FoodDeliveryApp.repository;

import com.foodDelivery.FoodDeliveryApp.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    List<Order> findByUserId(String userId);
    List<Order> findByRestaurentId(String restaurentId);
    List<Order> findByStatus(String Status);
    List<Order> findByUserIdAndStatus(String userId,String Status);
}
