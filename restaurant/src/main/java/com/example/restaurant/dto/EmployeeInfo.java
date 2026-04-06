package com.example.restaurant.dto;

import com.example.restaurant.model.EmployeeType;
import lombok.Data;

@Data
public class EmployeeInfo {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private EmployeeType employeeType;
    private String restaurantName;
}
