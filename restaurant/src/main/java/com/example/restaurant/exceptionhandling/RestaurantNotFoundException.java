package com.example.restaurant.exceptionhandling;

public class RestaurantNotFoundException extends RuntimeException {
    private final Long restaurantId;
    public RestaurantNotFoundException(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
    public Long getRestaurantId() {
        return restaurantId;
    }
}
