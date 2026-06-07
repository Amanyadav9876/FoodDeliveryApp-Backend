package com.foodDelivery.FoodDeliveryApp.service;

import com.foodDelivery.FoodDeliveryApp.model.MenuItem;
import com.foodDelivery.FoodDeliveryApp.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItem create(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getByRestaurantId(String restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public void delete(String id) {
        menuItemRepository.deleteById(id);
    }
}