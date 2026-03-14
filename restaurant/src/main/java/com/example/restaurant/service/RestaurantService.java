package com.example.restaurant.service;

import com.example.restaurant.dto.RestaurantCreateCommand;
import com.example.restaurant.dto.RestaurantInfo;
import com.example.restaurant.exceptionhandling.RestaurantNotFoundException;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public RestaurantService(RestaurantRepository restaurantRepository, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    public RestaurantInfo save(@Valid RestaurantCreateCommand command) {
        Restaurant restaurantToSave = modelMapper.map(command, Restaurant.class);
        Restaurant savedRestaurant = restaurantRepository.save(restaurantToSave);
        return modelMapper.map(savedRestaurant, RestaurantInfo.class);
    }
    public List<RestaurantInfo> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantInfo.class))
                .collect(Collectors.toList());
    }
    private Restaurant findById(Long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
    }
    public RestaurantInfo getById(Long id) {
        Restaurant restaurant = findById(id);
        return modelMapper.map(restaurant, RestaurantInfo.class);
    }
    public void deleteById(Long id) {
        Restaurant restaurantToDelete = findById(id);
        restaurantRepository.delete(restaurantToDelete);
    }
}
