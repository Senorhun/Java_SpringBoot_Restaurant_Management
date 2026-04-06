package com.example.restaurant.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemInfo {
    private Long id;
    private int quantity;
    private BigDecimal price;
    private String OrderItemName;
}
