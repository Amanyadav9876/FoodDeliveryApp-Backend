package com.foodDelivery.FoodDeliveryApp.controller;

import com.foodDelivery.FoodDeliveryApp.model.Order;
import com.foodDelivery.FoodDeliveryApp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.placeOrder(order));
    }

    @GetMapping("/my/{userId}")
    public ResponseEntity<?> getMyOrders(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getMyOrders(userId));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getRestaurantOrders(@PathVariable String restaurantId) {
        return ResponseEntity.ok(orderService.getRestaurantOrders(restaurantId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}