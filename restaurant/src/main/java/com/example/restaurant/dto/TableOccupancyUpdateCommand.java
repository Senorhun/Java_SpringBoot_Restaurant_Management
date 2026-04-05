package com.example.restaurant.dto;

import com.example.restaurant.model.TableStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TableOccupancyUpdateCommand {
    @NotNull
    private TableStatus tableStatus;
}
