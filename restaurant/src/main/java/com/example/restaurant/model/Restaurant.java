package com.example.restaurant.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;
    private int star;

    @OneToMany(mappedBy = "restaurant")
    private List<Employee> employeeList;

    @OneToMany(mappedBy = "restaurant")
    private List<MenuItem> menuItemList;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantTable> restaurantTableList;
}
