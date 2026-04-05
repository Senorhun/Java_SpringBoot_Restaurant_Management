package com.example.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TableCreateCommand {
    @NotNull
    private int number;
    @NotNull
    private Long restaurantId;
    private Integer capacity;
}
