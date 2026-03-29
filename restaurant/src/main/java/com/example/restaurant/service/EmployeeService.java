package com.example.restaurant.service;

import com.example.restaurant.dto.EmployeeCreateCommand;
import com.example.restaurant.dto.EmployeeInfo;
import com.example.restaurant.dto.EmployeeUpdateCommand;
import com.example.restaurant.exceptionhandling.EmployeeNotFoundException;
import com.example.restaurant.model.Employee;
import com.example.restaurant.model.EmployeeType;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper, RestaurantService restaurantService ) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.restaurantService = restaurantService;
    }
    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }
    public EmployeeInfo getEmployeeById(Long id) {
        Employee employee = findEmployeeById(id);
        EmployeeInfo employeeInfo = modelMapper.map(employee, EmployeeInfo.class);
        employeeInfo.setRestaurantName(employee.getRestaurant().getName());
        return employeeInfo;
    }
    public EmployeeInfo save(@Valid EmployeeCreateCommand command) {
        Employee employeeToSave = modelMapper.map(command, Employee.class);
        Restaurant restaurant = restaurantService.findById(command.getRestaurantId());
        employeeToSave.setRestaurant(restaurant);
        Employee savedEmployee = employeeRepository.save(employeeToSave);
        EmployeeInfo employeeInfo = modelMapper.map(savedEmployee, EmployeeInfo.class);
        employeeInfo.setRestaurantName(savedEmployee.getRestaurant().getName());
        return employeeInfo;
    }
    public List<EmployeeInfo> findAll(){
        return employeeRepository.findAll().stream()
                .map(employee -> {
                    EmployeeInfo employeeInfo = modelMapper.map(employee,EmployeeInfo.class);
                    employeeInfo.setRestaurantName(employee.getRestaurant().getName());
                    return employeeInfo;
                }).toList();
    }
    public void delete(Long id) {
        Employee employeeToDelete = findEmployeeById(id);
        employeeRepository.delete(employeeToDelete);
    }
    public EmployeeInfo updateEmployee(Long id, EmployeeUpdateCommand command) {
        Employee employeeToUpdate = findEmployeeById(id);
        modelMapper.map(command, employeeToUpdate);
        employeeRepository.save(employeeToUpdate);
        EmployeeInfo employeeInfo = modelMapper.map(employeeToUpdate, EmployeeInfo.class);
        Restaurant restaurant = restaurantService.findById(command.getRestaurantId());
        employeeInfo.setRestaurantName(restaurant.getName());
        return employeeInfo;
    }

    public List<EmployeeInfo> getByType(EmployeeType type) {
        List<Employee> employees = employeeRepository.getByType(type);
        return employees.stream()
                .map(employee -> {
                    EmployeeInfo employeeInfo = modelMapper.map(employee, EmployeeInfo.class );
                    employeeInfo.setRestaurantName(employee.getRestaurant().getName());
                    return employeeInfo;
                }).toList();
    }
}
