package com.foodDelivery.FoodDeliveryApp.service;

import com.foodDelivery.FoodDeliveryApp.model.User;
import com.foodDelivery.FoodDeliveryApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public String generateOtp() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public void saveOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        user.setOtp(otp);
        userRepository.save(user);
    }

    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (user.getOtp() != null && user.getOtp().equals(otp)) {
            user.setOtp(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Async
    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("KHAO - Email Verification");
        message.setText(
                "Tumhara OTP hai: " + otp +
                        "\n\n5 minutes mein expire ho jaayega." +
                        "\n\nKHAO Team"
        );
        mailSender.send(message);
    }

    @Async
    public void mailConfirmation(String email, String orderId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("KHAO - Order Confirmed!");
        message.setText(
                "Tumhara order place ho gaya hai!\n" +
                        "Order ID: " + orderId + "\n" +
                        "App mein track karo."
        );
        mailSender.send(message);
    }
}
