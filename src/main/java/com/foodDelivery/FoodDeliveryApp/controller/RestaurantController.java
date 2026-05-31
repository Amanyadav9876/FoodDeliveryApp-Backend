package com.foodDelivery.FoodDeliveryApp.controller;

import com.foodDelivery.FoodDeliveryApp.model.Restaurant;
import com.foodDelivery.FoodDeliveryApp.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;


    @GetMapping
    public ResponseEntity<?> getAll(){

        return ResponseEntity.ok(restaurantService.getAll());
    }

    @GetMapping("/{id}")
    public  ResponseEntity<?> getById(@PathVariable String id){

        return ResponseEntity.ok(restaurantService.getAll());
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Restaurant restaurant){
        return ResponseEntity.ok(restaurantService.create(restaurant));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable String id,
                                              @RequestBody Restaurant restaurant){
        return ResponseEntity.ok(restaurantService.updateRestaurant(id,restaurant));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok("Deleted");
    }



}
