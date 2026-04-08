package com.example.restaurant.service;

import com.example.restaurant.dto.*;
import com.example.restaurant.exceptionhandling.RestaurantNameDuplicationException;
import com.example.restaurant.exceptionhandling.RestaurantNotFoundException;
import com.example.restaurant.exceptionhandling.TableNotFoundException;
import com.example.restaurant.exceptionhandling.TableNumberDuplicationException;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.RestaurantTable;
import com.example.restaurant.model.TableStatus;
import com.example.restaurant.repository.EmployeeRepository;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.TableRepository;
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
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final TableRepository tableRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, ModelMapper modelMapper, EmployeeRepository employeeRepository, TableRepository tableRepository) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.tableRepository = tableRepository;
    }

    public RestaurantInfo save(@Valid RestaurantCreateCommand command) {
        if (restaurantRepository.existsByNameIgnoreCase(command.getName())) {
            throw new RestaurantNameDuplicationException(command.getName());
        }
        Restaurant restaurantToSave = modelMapper.map(command, Restaurant.class);
        Restaurant savedRestaurant = restaurantRepository.save(restaurantToSave);
        return modelMapper.map(savedRestaurant, RestaurantInfo.class);
    }
    public List<RestaurantInfo> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantInfo.class))
                .collect(Collectors.toList());
    }
    public Restaurant findById(Long id) {
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

    public List<EmployeeInfo> getEmployeesByRestaurant(Long restaurantId) {
        findById(restaurantId);
        return employeeRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(employee -> {
                    EmployeeInfo employeeInfo = modelMapper.map(employee, EmployeeInfo.class);
                    employeeInfo.setRestaurantName(employee.getRestaurant().getName());
                    return employeeInfo;
                })
                .toList();
    }

    public TableInfo installTable(@Valid TableCreateCommand command) {
        if (tableRepository.existsByNumber(command.getNumber())){
            throw new TableNumberDuplicationException(command.getNumber());
        }
        Restaurant restaurant = findById(command.getRestaurantId());
        RestaurantTable restaurantTableToSave = modelMapper.map(command, RestaurantTable.class);
        int capacity = command.getCapacity() != null ? command.getCapacity() : 2;
        restaurantTableToSave.setCapacity(capacity);
        restaurantTableToSave.setTableStatus(TableStatus.FREE);
        restaurantTableToSave.setRestaurant(restaurant);
        tableRepository.save(restaurantTableToSave);
        TableInfo tableInfo = modelMapper.map(restaurantTableToSave, TableInfo.class);
        tableInfo.setRestaurantName(restaurant.getName());
        return tableInfo;
    }

    public List<TableInfo> findAllTable() {
        List<RestaurantTable> restaurantTables = tableRepository.findAll();
        return restaurantTables.stream()
                .map(table -> {
                    TableInfo tableInfo = modelMapper.map(table, TableInfo.class);
                    tableInfo.setRestaurantName(table.getRestaurant().getName());
                    return tableInfo;
                }).toList();
    }
    public RestaurantTable findTableById(long id){
        return tableRepository.findById(id).orElseThrow(() -> new TableNotFoundException(id));
    }

    public TableInfo getTableById(Long id) {
        RestaurantTable restaurantTable = findTableById(id);
        TableInfo tableInfo = modelMapper.map(restaurantTable, TableInfo.class);
        tableInfo.setRestaurantName(restaurantTable.getRestaurant().getName());
        return tableInfo;
    }

    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    public TableInfo updateTable(Long id, @Valid TableUpdateCommand command) {
        RestaurantTable restaurantTableToSave = findTableById(id);
        modelMapper.map(command, restaurantTableToSave);
        restaurantTableToSave.setTableStatus(command.getTableStatus());
        RestaurantTable savedRestaurantTable = tableRepository.save(restaurantTableToSave);
        TableInfo tableInfo = modelMapper.map(savedRestaurantTable, TableInfo.class);
        tableInfo.setRestaurantName(savedRestaurantTable.getRestaurant().getName());
        return tableInfo;
    }

    public TableInfo updateTableOccupancy(Long id, @Valid TableOccupancyUpdateCommand command) {
        RestaurantTable restaurantTableToSave = findTableById(id);
        restaurantTableToSave.setTableStatus(command.getTableStatus());
        tableRepository.save(restaurantTableToSave);
        TableInfo tableInfo = modelMapper.map(restaurantTableToSave, TableInfo.class);
        tableInfo.setRestaurantName(restaurantTableToSave.getRestaurant().getName());
        return tableInfo;
    }

    public TableInfo updateTableCapacity(Long id, @Valid TableUpdateCapacityCommand command) {
        RestaurantTable restaurantTableToSave = findTableById(id);
        restaurantTableToSave.setCapacity(command.getCapacity());
        tableRepository.save(restaurantTableToSave);
        TableInfo tableInfo = modelMapper.map(restaurantTableToSave, TableInfo.class);
        tableInfo.setRestaurantName(restaurantTableToSave.getRestaurant().getName());
        return tableInfo;
    }
}
