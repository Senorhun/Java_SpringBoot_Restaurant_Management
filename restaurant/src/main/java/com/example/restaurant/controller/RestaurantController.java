package com.example.restaurant.controller;

import com.example.restaurant.dto.RestaurantCreateCommand;
import com.example.restaurant.dto.RestaurantInfo;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
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
        return new ResponseEntity<>(restaurantInfo, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<RestaurantInfo>> findAll(){
        List<RestaurantInfo> restaurantInfoList = restaurantService.findAll();
        return new ResponseEntity<>(restaurantInfoList, HttpStatus.OK);
    }
}
