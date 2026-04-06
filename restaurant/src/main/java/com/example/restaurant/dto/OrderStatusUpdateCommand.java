package com.example.restaurant.dto;

import com.example.restaurant.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdateCommand {
    @NotNull
    private OrderStatus orderStatus;
}
