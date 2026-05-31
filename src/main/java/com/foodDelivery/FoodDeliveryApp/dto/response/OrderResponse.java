package com.foodDelivery.FoodDeliveryApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private String id;
    private String restaurantId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status;
    private String deliveryAddress;
    private LocalDateTime orderTime;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        private String itemName;
        private int quantity;
        private double price;
    }
}