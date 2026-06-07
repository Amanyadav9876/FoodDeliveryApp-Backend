package com.foodDelivery.FoodDeliveryApp.controller;

import com.foodDelivery.FoodDeliveryApp.model.MenuItem;
import com.foodDelivery.FoodDeliveryApp.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(menuItemService.getByRestaurantId(restaurantId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(menuItemService.create(menuItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        menuItemService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}