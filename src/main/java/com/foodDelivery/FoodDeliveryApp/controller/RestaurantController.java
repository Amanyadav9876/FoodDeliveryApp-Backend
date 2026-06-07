package com.foodDelivery.FoodDeliveryApp.controller;

import com.foodDelivery.FoodDeliveryApp.model.Restaurant;
import com.foodDelivery.FoodDeliveryApp.model.User;
import com.foodDelivery.FoodDeliveryApp.repository.UserRepository;
import com.foodDelivery.FoodDeliveryApp.service.RestaurantService;
import com.foodDelivery.FoodDeliveryApp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(restaurantService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/my-restaurants")
    public ResponseEntity<?> getMyRestaurants(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(restaurantService.getMyRestaurants(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Restaurant restaurant,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // ownerId aur ownerEmail dono set karo
            restaurant.setOwnerId(user.getId());
            restaurant.setOwnerEmail(email); // ← yeh add kiya

            return ResponseEntity.ok(restaurantService.create(restaurant));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Restaurant create nahi hua: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(
            @PathVariable String id,
            @RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok("Deleted");
    }
}