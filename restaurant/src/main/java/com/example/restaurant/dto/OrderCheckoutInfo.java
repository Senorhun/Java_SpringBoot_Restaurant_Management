package com.example.restaurant.dto;

import com.example.restaurant.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderCheckoutInfo {
    private Long id;
    private Long restaurantTableId;
    private int tableNumber;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime paidAt;
    private List<OrderItemInfo> items;
}
