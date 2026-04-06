package com.example.restaurant.exceptionhandling;

public class OrderItemNotFoundException extends RuntimeException {
  private final Long id;
  public OrderItemNotFoundException(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
