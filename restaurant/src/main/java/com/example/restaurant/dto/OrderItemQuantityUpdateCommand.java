package com.example.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemQuantityUpdateCommand {
    @NotNull
    private int quantity;
}
