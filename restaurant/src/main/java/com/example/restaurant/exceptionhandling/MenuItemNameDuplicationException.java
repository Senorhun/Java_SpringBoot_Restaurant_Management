package com.example.restaurant.exceptionhandling;

public class MenuItemNameDuplicationException extends RuntimeException {
  private final String name;
  public MenuItemNameDuplicationException(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
