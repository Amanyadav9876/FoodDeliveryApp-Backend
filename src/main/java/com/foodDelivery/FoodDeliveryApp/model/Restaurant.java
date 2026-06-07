package com.foodDelivery.FoodDeliveryApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="restaurants")
public class Restaurant implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    private String id;
    private String name;
    private String address;
    private String phone;
    private String ownerEmail; // ← ye add karo

    private String ownerId;
    private  String city;
    private  boolean Open;
    private  String cuisineType;
    private  double rating=0.0;

}
