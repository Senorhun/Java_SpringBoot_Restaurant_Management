package com.example.restaurant.repository;

import com.example.restaurant.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    boolean existsByNumber(int number);
}
