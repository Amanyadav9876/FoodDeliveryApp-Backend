package com.foodDelivery.FoodDeliveryApp.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class LoginRequest {
    @Email(message = "valid email")
    @NotBlank(message = "email required")
    private String email;

    @NotBlank(message = "password required")
    private String password;
}
