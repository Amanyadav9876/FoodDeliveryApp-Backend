package com.foodDelivery.FoodDeliveryApp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name required hai")
    private String name;

    @Email(message = "Valid email daalo")
    @NotBlank(message = "Email required hai")
    private String email;

    @NotBlank(message = "Password required hai")
    @Size(min = 6, message = "Password 6 characters ka hona chahiye")
    private String password;

    private String phone;

    private String role; // CUSTOMER, RESTAURANT_OWNER
}