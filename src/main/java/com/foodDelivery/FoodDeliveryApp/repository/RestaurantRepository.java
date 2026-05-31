package com.foodDelivery.FoodDeliveryApp.repository;

import com.foodDelivery.FoodDeliveryApp.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant,String> {
    List<Restaurant> findByCity(String city);
    List<Restaurant> findByOwnerId(String ownerId);
    List<Restaurant> findByCuisineType(String cuisineType);
    boolean existsByNameAndCity(String name,String city);
}
