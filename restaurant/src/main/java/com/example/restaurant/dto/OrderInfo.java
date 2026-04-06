package com.example.restaurant.dto;

import com.example.restaurant.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderInfo {
    private Long id;
    private Long tableId;              // az asztal azonosítója
    private int tableNumber;           // az asztal száma
    private BigDecimal totalPrice;
    private OrderStatus status;
    private List<OrderItemInfo> items; // a rendelés tétel listája
}
