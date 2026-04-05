package com.example.restaurant.repository;

import com.example.restaurant.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {
    boolean existsByNumber(int number);
}
