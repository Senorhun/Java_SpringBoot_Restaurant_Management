package com.example.restaurant.exceptionhandling;

public class OrderAlreadyPaidException extends RuntimeException {
    private final Long id;
    public OrderAlreadyPaidException(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
}
