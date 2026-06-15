package com.foodDelivery.FoodDeliveryApp.service;

import com.foodDelivery.FoodDeliveryApp.dto.request.LoginRequest;
import com.foodDelivery.FoodDeliveryApp.dto.request.RegisterRequest;
import com.foodDelivery.FoodDeliveryApp.dto.response.AuthResponse;
import com.foodDelivery.FoodDeliveryApp.exception.EmailAlreadyRegisteredException;
import com.foodDelivery.FoodDeliveryApp.model.User;
import com.foodDelivery.FoodDeliveryApp.repository.UserRepository;
import com.foodDelivery.FoodDeliveryApp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setEmailVerified(true); // Direct verified

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole(),
                "Registration successful!",
                user.getId()
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password!");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole(),
                "Login Successful!",
                user.getId()
        );
    }
}
