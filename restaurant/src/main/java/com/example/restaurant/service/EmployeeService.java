package com.example.restaurant.service;

import com.example.restaurant.dto.EmployeeCreateCommand;
import com.example.restaurant.dto.EmployeeInfo;
import com.example.restaurant.model.Employee;
import com.example.restaurant.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public EmployeeInfo save(@Valid EmployeeCreateCommand command) {
        Employee employeeToSave = modelMapper.map(command, Employee.class);
        Employee savedEmployee = employeeRepository.save(employeeToSave);
        return modelMapper.map(savedEmployee, EmployeeInfo.class);
    }
    public List<EmployeeInfo> listEmployees(){
        return employeeRepository.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeInfo.class))
                .collect(Collectors.toList());
    }
}
