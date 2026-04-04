package com.example.restaurant.exceptionhandling;

public class RestaurantNameDuplicationException extends RuntimeException {
    private final String name;
    public RestaurantNameDuplicationException(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
