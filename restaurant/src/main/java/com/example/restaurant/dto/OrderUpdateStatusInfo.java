package com.example.restaurant.dto;

import com.example.restaurant.model.OrderStatus;
import lombok.Data;

@Data
public class OrderUpdateStatusInfo {
    private Long id;
    private OrderStatus orderStatus;
}
