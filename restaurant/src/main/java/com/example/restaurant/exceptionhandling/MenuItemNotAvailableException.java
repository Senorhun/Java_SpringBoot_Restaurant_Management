package com.example.restaurant.exceptionhandling;

public class MenuItemNotAvailableException extends RuntimeException{
    private final Long menuItemId;
    public MenuItemNotAvailableException(Long menuItemId) {
        this.menuItemId = menuItemId;
    }
    public Long getMenuItemId() {
        return menuItemId;
    }
}
