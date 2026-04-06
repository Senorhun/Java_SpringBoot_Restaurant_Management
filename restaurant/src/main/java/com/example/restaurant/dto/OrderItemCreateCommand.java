package com.example.restaurant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemCreateCommand {
    @NotNull
    @Min(1)
    private int quantity;
    @NotNull
    private int menuItemId;

}
