package com.example.restaurant.controller;

import com.example.restaurant.dto.MenuItemCreateCommand;
import com.example.restaurant.dto.MenuItemInfo;
import com.example.restaurant.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuItemInfo> save(@Valid @RequestBody MenuItemCreateCommand command) {
        MenuItemInfo menuItemInfo = menuService.save(command);
        return new ResponseEntity<>(menuItemInfo, HttpStatus.CREATED);
    }

}
