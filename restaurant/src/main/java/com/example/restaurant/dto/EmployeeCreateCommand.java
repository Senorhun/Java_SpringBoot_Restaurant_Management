package com.example.restaurant.dto;

import com.example.restaurant.model.EmployeeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeCreateCommand {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private EmployeeType employeeType;

    private Long restaurant_id;
}
