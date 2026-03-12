package com.example.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantCreateCommand {
    @NotBlank
    private String name;
    @NotNull
    private int capacity;
    @NotNull
    private int star;
}
