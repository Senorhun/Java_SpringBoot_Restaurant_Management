package com.example.restaurant.dto;

import com.example.restaurant.model.Restaurant;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class TableInfo {
    private int number;
    private int capacity;
    private boolean occupied;
    private String restaurantName;
}
