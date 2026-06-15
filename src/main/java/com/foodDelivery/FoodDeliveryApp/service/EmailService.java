package com.foodDelivery.FoodDeliveryApp.service;

import com.foodDelivery.FoodDeliveryApp.model.User;
import com.foodDelivery.FoodDeliveryApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private UserRepository userRepository;

    @Value("${mailersend.api.key}")
    private String mailersendApiKey;

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
            RestTemplate restTemplate = new RestTemplate();
            String body = """
                {
                    "from": {"email": "MS_test@trial-3z0vklo1rewldpyo.mlsender.net", "name": "KHAO App"},
                    "to": [{"email": "%s"}],
                    "subject": "KHAO - Email Verification OTP",
                    "text": "Tumhara OTP hai: %s\\n\\n5 minutes mein expire ho jaayega.\\n\\nKHAO Team"
                }
                """.formatted(toEmail, otp);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(mailersendApiKey);

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.mailersend.com/v1/email",
                request,
                String.class
            );
            System.out.println("OTP sent to: " + toEmail + " | Status: " + response.getStatusCode());
        } catch (Exception e) {
            System.out.println("OTP Email failed: " + e.getMessage());
        }
    }

    @Async
    public void mailConfirmation(String email, String orderId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String body = """
                {
                    "from": {"email": "MS_test@trial-3z0vklo1rewldpyo.mlsender.net", "name": "KHAO App"},
                    "to": [{"email": "%s"}],
                    "subject": "KHAO - Order Confirmed!",
                    "text": "Tumhara order place ho gaya hai!\\nOrder ID: %s\\nApp mein track karo.\\n\\nKHAO Team"
                }
                """.formatted(email, orderId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(mailersendApiKey);

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(
                "https://api.mailersend.com/v1/email",
                request,
                String.class
            );
            System.out.println("Order confirmation sent to: " + email);
        } catch (Exception e) {
            System.out.println("Order email failed: " + e.getMessage());
        }
    }
}
