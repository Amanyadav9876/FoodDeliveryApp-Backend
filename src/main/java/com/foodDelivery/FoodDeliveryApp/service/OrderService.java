package com.foodDelivery.FoodDeliveryApp.service;

import com.foodDelivery.FoodDeliveryApp.model.Order;
import com.foodDelivery.FoodDeliveryApp.repository.OrderRepository;
import com.foodDelivery.FoodDeliveryApp.repository.RestaurantRepository;
import com.foodDelivery.FoodDeliveryApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public Order placeOrder(Order order) {
        order.setStatus("PLACED");
        Order saved = orderRepository.save(order);
        sendEmailToOwner(saved);
        return saved;
    }

    private void sendEmailToOwner(Order order) {
        try {
            // Customer details fetch karo
            String customerName = "Unknown Customer";
            String customerEmail = "Unknown";
            String customerPhone = "N/A";

            if (order.getUserId() != null && !order.getUserId().isEmpty()) {
                var user = userRepository.findById(order.getUserId());
                if (user.isPresent()) {
                    customerName = user.get().getName() != null ? user.get().getName() : "Unknown";
                    customerEmail = user.get().getEmail() != null ? user.get().getEmail() : "Unknown";
                    customerPhone = user.get().getPhone() != null ? user.get().getPhone() : "N/A";
                }
            }

            // Owner email fetch karo — pehle ownerEmail try karo, phir ownerId se
            String ownerEmail = null;

            if (order.getRestaurentId() != null && !order.getRestaurentId().isEmpty()) {
                var restaurant = restaurantRepository.findById(order.getRestaurentId());
                if (restaurant.isPresent()) {
                    // Pehle ownerEmail directly check karo
                    if (restaurant.get().getOwnerEmail() != null &&
                            !restaurant.get().getOwnerEmail().isEmpty()) {
                        ownerEmail = restaurant.get().getOwnerEmail();
                        System.out.println("✅ Owner email restaurant se mila: " + ownerEmail);
                    }
                    // Agar ownerEmail nahi toh ownerId se dhundo
                    else if (restaurant.get().getOwnerId() != null) {
                        var owner = userRepository.findById(restaurant.get().getOwnerId());
                        if (owner.isPresent()) {
                            ownerEmail = owner.get().getEmail();
                            System.out.println("✅ Owner email ownerId se mila: " + ownerEmail);
                        }
                    }
                }
            }

            // Agar phir bhi null hai toh customer ko hi bhejo
            if (ownerEmail == null || ownerEmail.isEmpty()) {
                ownerEmail = customerEmail;
                System.out.println("⚠️ Owner email nahi mila — customer ko bhej rahe hain: " + ownerEmail);
            }

            System.out.println("📧 Final owner email: " + ownerEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("yadavamanindra123@gmail.com");
            message.setTo(ownerEmail);
            message.setSubject("🍛 Naya Order Aaya! Order #" + order.getId());
            message.setText(
                    "===============================\n" +
                            "KHAO - NEW ORDER ALERT! 🍛\n" +
                            "===============================\n\n" +
                            "Order ID: " + order.getId() + "\n" +
                            "Time: " + order.getOrderTime() + "\n\n" +
                            "CUSTOMER DETAILS:\n" +
                            "   Name    : " + customerName + "\n" +
                            "   Email   : " + customerEmail + "\n" +
                            "   Phone   : " + customerPhone + "\n\n" +
                            "Delivery Address:\n" +
                            "   " + order.getDeliveryAddress() + "\n\n" +
                            "Items    : " + (order.getItems() != null ? order.getItems().size() : 0) + " items\n" +
                            "Total    : Rs." + order.getTotalAmount() + "\n\n" +
                            "Dashboard: http://localhost:3001/owner\n\n" +
                            "===============================\n" +
                            "KHAO Food Delivery\n" +
                            "==============================="
            );

            mailSender.send(message);
            System.out.println("✅ Email send ho gaya: " + ownerEmail);

        } catch (Exception e) {
            System.out.println("❌ Email send nahi hua: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Order> getMyOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getRestaurantOrders(String restaurantId) {
        return orderRepository.findByRestaurentId(restaurantId);
    }

    public Order updateStatus(String id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}