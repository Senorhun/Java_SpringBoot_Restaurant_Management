package com.example.restaurant.exceptionhandling;

public class OrderNotFoundException extends RuntimeException {
  private final Long id;
  public OrderNotFoundException(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
