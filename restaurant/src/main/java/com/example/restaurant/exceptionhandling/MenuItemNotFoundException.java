package com.example.restaurant.exceptionhandling;

public class MenuItemNotFoundException extends RuntimeException{
    private final Long menuItemId;
    public MenuItemNotFoundException(Long menuItemId) {
        this.menuItemId = menuItemId;
    }
    public Long getMenuItemId() {
        return menuItemId;
    }
}
