package com.example.restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateCommand {
    @NotNull
    private Long restaurantTableId;

    @NotNull
    @Valid
    private List<OrderItemCreateCommand> orderItems;
}