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
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

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
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("yadavamanindra123@gmail.com");
            message.setTo(toEmail);
            message.setSubject("KHAO - Email Verification OTP");
            message.setText("Tumhara OTP hai: " + otp + "\n\n5 minutes mein expire ho jaayega.\n\nKHAO Team");
            mailSender.send(message);
            System.out.println("OTP sent to: " + toEmail);
        } catch (Exception e) {
            System.out.println("OTP Email failed: " + e.getMessage());
        }
    }

    @Async
    public void mailConfirmation(String email, String orderId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("yadavamanindra123@gmail.com");
            message.setTo(email);
            message.setSubject("KHAO - Order Confirmed!");
            message.setText("Tumhara order place ho gaya hai!\nOrder ID: " + orderId + "\nApp mein track karo.\n\nKHAO Team");
            mailSender.send(message);
            System.out.println("Order confirmation sent to: " + email);
        } catch (Exception e) {
            System.out.println("Order email failed: " + e.getMessage());
        }
    }
}
