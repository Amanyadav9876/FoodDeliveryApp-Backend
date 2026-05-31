package com.foodDelivery.FoodDeliveryApp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {

    @NotBlank(message = "Restaurant ID required hai")
    private String restaurantId;

    @NotEmpty(message = "Items required hain")
    private List<OrderItemRequest> items;

    @NotBlank(message = "Delivery address required hai")
    private String deliveryAddress;

    @Data
    public static class OrderItemRequest {
        private String menuItemId;
        private int quantity;
    }
}