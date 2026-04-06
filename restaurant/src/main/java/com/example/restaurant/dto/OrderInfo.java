package com.example.restaurant.dto;

import com.example.restaurant.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderInfo {
    private Long id;
    private Long restaurantTableId;
    private int tableNumber;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private List<OrderItemInfo> items;
}
