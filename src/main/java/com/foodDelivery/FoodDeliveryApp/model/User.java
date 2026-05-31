package com.foodDelivery.FoodDeliveryApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private  String name;

    @Indexed(unique = true)
    private String email;
    private String password;
    private String role;
    private String phone;
    private boolean emailVerified=false;
    private String otp;



}
