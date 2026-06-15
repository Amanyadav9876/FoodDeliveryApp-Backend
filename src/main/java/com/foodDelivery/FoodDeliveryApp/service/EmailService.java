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

    @Value("${resend.api.key}")
    private String resendApiKey;

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
                    "from": "KHAO App <onboarding@resend.dev>",
                    "to": ["yadavamanindra123@gmail.com"],
                    "subject": "KHAO - Email Verification OTP",
                    "text": "Tumhara OTP hai: %s\\n\\n5 minutes mein expire ho jaayega.\\n\\nKHAO Team"
                }
                """.formatted(otp);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.resend.com/emails",
                request,
                String.class
            );
            System.out.println("Email sent! Response: " + response.getStatusCode());
        } catch (Exception e) {
            System.out.println("Email send failed: " + e.getMessage());
        }
    }

    @Async
    public void mailConfirmation(String email, String orderId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String body = """
                {
                    "from": "KHAO App <onboarding@resend.dev>",
                    "to": ["yadavamanindra123@gmail.com"],
                    "subject": "KHAO - Order Confirmed!",
                    "text": "Tumhara order place ho gaya hai!\\nOrder ID: %s\\nApp mein track karo."
                }
                """.formatted(orderId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(
                "https://api.resend.com/emails",
                request,
                String.class
            );
        } catch (Exception e) {
            System.out.println("Order email failed: " + e.getMessage());
        }
    }
}
