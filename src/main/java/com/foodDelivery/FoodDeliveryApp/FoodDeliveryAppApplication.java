package com.foodDelivery.FoodDeliveryApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FoodDeliveryAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodDeliveryAppApplication.class, args);
    }
}
