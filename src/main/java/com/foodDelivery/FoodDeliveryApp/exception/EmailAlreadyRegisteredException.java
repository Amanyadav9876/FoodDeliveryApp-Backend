// com/foodDelivery/FoodDeliveryApp/exception/EmailAlreadyRegisteredException.java
package com.foodDelivery.FoodDeliveryApp.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException() {
        super("Email already registered!");
    }
}