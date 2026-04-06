package com.example.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuItemUpdateAvailabilityCommand {
    @NotNull
    private boolean available;
}
