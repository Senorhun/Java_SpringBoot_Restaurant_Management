package com.example.restaurant.repository;

import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.MenuItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem,Long> {
    @Query("select m from MenuItem m where m.menuItemType=:type and m.available=true")
    List<MenuItem> getByType(@Param("type") MenuItemType type);

    boolean existsByNameIgnoreCase(String name);
    Page<MenuItem> findByAvailableTrue(Pageable pageable);

}
