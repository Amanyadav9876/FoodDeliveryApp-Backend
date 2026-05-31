package com.foodDelivery.FoodDeliveryApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public String generateOtp(){
        Random random=new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }
    public void saveOtp(String email,String otp){
        redisTemplate.opsForValue()
                .set("otp"+email,otp,5, TimeUnit.MINUTES);

    }
    public boolean verifyOtp(String email,String otp){
        String savedOtp=(String) redisTemplate
                .opsForValue()
                .get("otp"+email);
        if(savedOtp!=null && savedOtp.equals(otp)){
            redisTemplate.delete("otp" + email);
            return true;
        }
        return false;
    }
    public void sendOtpEmail(String email,String otp){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Food delivery - email Varification");
        message.setText(
                "tumhara otp" + otp +
                        "\n5 min me expire ho jaayega."
        );
        mailSender.send(message);
    }
    public void mailConfirmation(String email,String orderId){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("order confirmed");
        message.setText(
                "tumharea order placed ho gya hai\n"
                + "OrderId" + orderId + "\n" +
                        "track kara app me"
        );
        mailSender.send(message);
    }


}
