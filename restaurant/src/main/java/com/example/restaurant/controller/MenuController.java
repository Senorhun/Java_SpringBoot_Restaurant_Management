package com.example.restaurant.controller;

import com.example.restaurant.dto.MenuItemCreateCommand;
import com.example.restaurant.dto.MenuItemInfo;
import com.example.restaurant.dto.MenuItemUpdateAvailabilityCommand;
import com.example.restaurant.dto.MenuItemUpdateCommand;
import com.example.restaurant.model.MenuItemType;
import com.example.restaurant.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public ResponseEntity<List<MenuItemInfo>> findAll() {
        List<MenuItemInfo> menuItemInfos = menuService.findAll();
        return new ResponseEntity<>(menuItemInfos, HttpStatus.OK);
    }
    @GetMapping("/type")
    public ResponseEntity<List<MenuItemInfo>> findByType(@RequestParam(value = "type") MenuItemType type) {
        List<MenuItemInfo> menuItemInfos = menuService.getByType(type);
        return new ResponseEntity<>(menuItemInfos, HttpStatus.OK);
    }
    @GetMapping("/available")
    public ResponseEntity<List<MenuItemInfo>> findAllAvailable() {
        List<MenuItemInfo> menuItemInfos = menuService.findAllAvailable();
        return new ResponseEntity<>(menuItemInfos, HttpStatus.OK);
    }
    @PatchMapping("/{id}/available")
    public ResponseEntity<MenuItemInfo> updateAvailable(@PathVariable("id") Long id, @Valid @RequestBody MenuItemUpdateAvailabilityCommand command) {
        MenuItemInfo menuItemInfo = menuService.updateAvailability(id, command);
        return new ResponseEntity<>(menuItemInfo, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemInfo> findById(@PathVariable Long id) {
        MenuItemInfo menuItemInfo = menuService.getMenuItemById(id);
        return new ResponseEntity<>(menuItemInfo, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemInfo> update(@PathVariable Long id, @Valid @RequestBody MenuItemUpdateCommand command) {
        MenuItemInfo menuItemInfo = menuService.updateMenuItem(id, command);
        return new ResponseEntity<>(menuItemInfo, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
