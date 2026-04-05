package com.example.restaurant.controller;

import com.example.restaurant.dto.*;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.Table;
import com.example.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @PostMapping
    public ResponseEntity<RestaurantInfo> save(@Valid @RequestBody RestaurantCreateCommand command) {
        RestaurantInfo restaurantInfo = restaurantService.save(command);
        return new ResponseEntity<>(restaurantInfo, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<RestaurantInfo>> findAll(){
        List<RestaurantInfo> restaurantInfoList = restaurantService.findAll();
        return new ResponseEntity<>(restaurantInfoList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantInfo> findById(@PathVariable Long id) {
        RestaurantInfo restaurantInfo = restaurantService.getById(id);
        return new ResponseEntity<>(restaurantInfo, HttpStatus.FOUND);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        restaurantService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{restaurantId}/employees")
    public List<EmployeeInfo> getEmployeesByRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.getEmployeesByRestaurant(restaurantId);
    }
    @PostMapping("/table")
    public ResponseEntity<TableInfo> installTable(@Valid @RequestBody TableCreateCommand command){
        TableInfo tableInfo = restaurantService.installTable(command);
        return new ResponseEntity<>(tableInfo, HttpStatus.CREATED);
    }
    @GetMapping("/table")
    public ResponseEntity<List<TableInfo>> getAllTables(){
        List<TableInfo> tableInfos = restaurantService.findAllTable();
        return new ResponseEntity<>(tableInfos, HttpStatus.OK);
    }
    @GetMapping("/table/{id}")
    public ResponseEntity<TableInfo> getTableById(@PathVariable Long id){
        TableInfo tableInfo = restaurantService.getTableById(id);
        return new ResponseEntity<>(tableInfo,HttpStatus.OK);
    }
    @DeleteMapping("/table/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id){
        restaurantService.deleteTable(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/table/{id}")
    public ResponseEntity<TableInfo> updateTable(@PathVariable Long id, @Valid @RequestBody TableUpdateCommand command){
        TableInfo tableInfo = restaurantService.updateTable(id, command);
        return new ResponseEntity<>(tableInfo,HttpStatus.OK);
    }
}
