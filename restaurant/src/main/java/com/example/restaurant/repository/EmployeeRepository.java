package com.example.restaurant.repository;

import com.example.restaurant.model.Employee;
import com.example.restaurant.model.EmployeeType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByRestaurantId(Long restaurantId);

    @Query("select e from Employee e where e.employeeType=:type")
    List<Employee> getByType(@Param("type") EmployeeType type);

    boolean existsByNicknameIgnoreCase(String name);
    boolean existsByEmailIgnoreCase(String email);
}
