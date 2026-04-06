package com.example.restaurant.dto;

import com.example.restaurant.model.MenuItemType;
import lombok.Data;

@Data
public class MenuItemInfo {
    private Long id;
    private String name;
    private double price;
    private MenuItemType menuItemType;
    private String description;
    private String restaurantName;
    private boolean available;
}
