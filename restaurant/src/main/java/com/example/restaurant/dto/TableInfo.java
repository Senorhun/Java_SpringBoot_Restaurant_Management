package com.example.restaurant.dto;

import com.example.restaurant.model.TableStatus;
import lombok.Data;

@Data
public class TableInfo {
    private int number;
    private int capacity;
    private TableStatus tableStatus;
    private String restaurantName;
}
