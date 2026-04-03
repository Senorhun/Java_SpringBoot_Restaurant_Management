package com.example.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MenuItemUpdateAvailabilityCommand {
    @NotBlank
    private boolean available;
}
