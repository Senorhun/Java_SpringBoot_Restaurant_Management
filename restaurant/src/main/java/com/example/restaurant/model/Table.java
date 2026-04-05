package com.example.restaurant.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@jakarta.persistence.Table(name = "restaurant_table")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int capacity;
    private TableStatus tableStatus;

    @Column(unique = true)
    private int number;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
