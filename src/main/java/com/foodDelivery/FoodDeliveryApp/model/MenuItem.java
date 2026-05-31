package com.foodDelivery.FoodDeliveryApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "menuItems")
public class MenuItem {
    @Id
    private  String id;
    private  String name;
    private  double price;
    private String category;
    private String description;
    private String restaurantId;
    private  String imageURL;
    private  boolean isAvailable;
}
