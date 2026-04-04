package com.example.restaurant.dto;

import com.example.restaurant.model.EmployeeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeUpdateCommand {
    @NotBlank
    private String name;
    @NotBlank
    private String nickname;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private EmployeeType employeeType;
    @NotNull
    private Long restaurantId;
}
