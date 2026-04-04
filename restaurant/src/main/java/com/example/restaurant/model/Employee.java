package com.example.restaurant.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;


}
