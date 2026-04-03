package com.example.restaurant.dto;

import com.example.restaurant.model.MenuItemType;
import lombok.Data;

@Data
public class MenuItemInfo {
    private String name;
    private double price;
    private MenuItemType menuItemType;
}
