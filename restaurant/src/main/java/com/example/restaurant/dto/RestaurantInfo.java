package com.example.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantInfo {
    private Long id;
    private String name;
    private int capacity;
    private int star;
}
