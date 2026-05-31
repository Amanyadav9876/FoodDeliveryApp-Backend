package com.foodDelivery.FoodDeliveryApp.service;

import com.foodDelivery.FoodDeliveryApp.model.Restaurant;
import com.foodDelivery.FoodDeliveryApp.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // Restaurant add karo
    public Restaurant addRestaurant(Restaurant restaurant) {

        // Duplicate check karo
        if (restaurantRepository.existsByNameAndCity(
                restaurant.getName(), restaurant.getCity())) {
            throw new RuntimeException(
                    "Restaurant already exists!");
        }
        return restaurantRepository.save(restaurant);
    }

    // Sab restaurants dekho — Redis me cache hoga
    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    // City se dhundo — Redis me cache hoga
    @Cacheable(value = "restaurants", key = "#city")
    public List<Restaurant> getRestaurantsByCity(String city) {
        return restaurantRepository.findByCity(city);
    }




    // ID se dhundo
    public Restaurant getRestaurantById(String id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Restaurant not found!"));
    }

    // Owner ke restaurants
    public List<Restaurant> getMyRestaurants(String ownerId) {
        return restaurantRepository.findByOwnerId(ownerId);
    }

    // Restaurant update karo
    public Restaurant updateRestaurant(
            String id, Restaurant updated) {
        Restaurant existing = getRestaurantById(id);
        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        existing.setCity(updated.getCity());
        existing.setPhone(updated.getPhone());
        existing.setCuisineType(updated.getCuisineType());
        existing.setOpen(updated.isOpen());
        return restaurantRepository.save(existing);
    }

    // Restaurant delete karo
    public void deleteRestaurant(String id) {
        restaurantRepository.deleteById(id);
    }
}