package com.example.restaurant.repository;

import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.MenuItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem,Long> {
    boolean existsByNameIgnoreCase(String name);
    Page<MenuItem> findByAvailableTrue(Pageable pageable);
    Page<MenuItem> findByMenuItemTypeAndAvailableTrue(MenuItemType type, Pageable pageable);

}
