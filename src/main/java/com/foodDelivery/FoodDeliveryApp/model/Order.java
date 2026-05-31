package com.foodDelivery.FoodDeliveryApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Orders")
public class Order {
    @Id
    private  String id;
    private  String restaurentId;
    private  String userId;
    private  String item;
    private List<OrderItem> items;
    private  double totalAmount;
    private String status;
    private String deliveryAddress;
    private LocalDateTime orderTime=LocalDateTime.now();
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem{
        private String menuItemId;
        private String itemName;
        private String quantity;
        private double price;
    }


}
