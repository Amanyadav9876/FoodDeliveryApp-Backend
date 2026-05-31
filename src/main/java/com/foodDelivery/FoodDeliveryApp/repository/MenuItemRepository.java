package com.foodDelivery.FoodDeliveryApp.repository;

import com.foodDelivery.FoodDeliveryApp.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemRepository
        extends MongoRepository<MenuItem, String> {

    List<MenuItem> findByRestaurantId(
            String restaurantId);

    List<MenuItem> findByRestaurantIdAndCategory(
            String restaurantId,
            String category);

    List<MenuItem> findByRestaurantIdAndIsAvailable(
            String restaurantId,
            boolean isAvailable);
}