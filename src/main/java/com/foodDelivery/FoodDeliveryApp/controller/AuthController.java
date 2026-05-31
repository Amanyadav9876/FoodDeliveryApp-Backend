package com.foodDelivery.FoodDeliveryApp.controller;

import com.foodDelivery.FoodDeliveryApp.dto.request.LoginRequest;
import com.foodDelivery.FoodDeliveryApp.dto.request.RegisterRequest;
import com.foodDelivery.FoodDeliveryApp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email,
                                       @RequestParam String otp) {
        return ResponseEntity.ok(authService.verifyOtp(email, otp));
    }

}