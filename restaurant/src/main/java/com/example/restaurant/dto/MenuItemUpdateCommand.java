package com.example.restaurant.dto;

import com.example.restaurant.model.MenuItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuItemUpdateCommand {
    @NotBlank
    private String name;
    @NotNull
    private double price;
    @NotNull
    private MenuItemType menuItemType;
    private String description;
}
