package com.example.restaurant.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@jakarta.persistence.Table(name = "restaurant_table")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int capacity;

    @Enumerated(EnumType.STRING)
    private TableStatus tableStatus;

    @Column(unique = true)
    private int number;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "restaurantTable")
    private List<Orders> orders;

}
