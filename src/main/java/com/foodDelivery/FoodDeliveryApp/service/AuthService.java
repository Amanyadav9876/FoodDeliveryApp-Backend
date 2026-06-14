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

    @Autowired
    private EmailService emailService;

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
        user.setEmailVerified(false);

        // OTP generate karo
        String otp = emailService.generateOtp();
        user.setOtp(otp);
        userRepository.save(user);

        // Email bhejo
        try {
            emailService.sendOtpEmail(request.getEmail(), otp);
        } catch (Exception e) {
            System.out.println("Email send failed: " + e.getMessage());
        }

        return new AuthResponse(
                null,
                user.getEmail(),
                user.getName(),
                user.getRole(),
                "OTP bheja gaya hai email pe! Verify karo.",
                null
        );
    }

    public AuthResponse verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!emailService.verifyOtp(email, otp)) {
            throw new RuntimeException("Invalid OTP!");
        }

        user.setEmailVerified(true);
        user.setOtp(null);
        userRepository.save(user);

        String token = jwtUtil.generateToken(email);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole(),
                "Email verified! Login ho gaye ho.",
                user.getId()
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email verify karo pehle!");
        }

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