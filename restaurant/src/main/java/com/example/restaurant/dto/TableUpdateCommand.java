package com.example.restaurant.dto;

import com.example.restaurant.model.TableStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TableUpdateCommand {
    @NotNull
    private int number;
    @NotNull
    private Long restaurantId;
    private Integer capacity;
    private TableStatus tableStatus;
}
