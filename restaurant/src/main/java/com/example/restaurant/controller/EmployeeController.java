package com.example.restaurant.controller;

import com.example.restaurant.dto.EmployeeCreateCommand;
import com.example.restaurant.dto.EmployeeInfo;
import com.example.restaurant.dto.EmployeeUpdateCommand;
import com.example.restaurant.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeInfo> save(@Valid @RequestBody EmployeeCreateCommand command) {
        EmployeeInfo employeeInfo = employeeService.save(command);
        return new ResponseEntity<>(employeeInfo, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeInfo> getById(@PathVariable Long id) {
        EmployeeInfo employeeInfo = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employeeInfo, HttpStatus.FOUND);
    }
    @GetMapping
    public ResponseEntity<List<EmployeeInfo>> findAll() {
        List<EmployeeInfo> employeeInfoList = employeeService.findAll();
        return new ResponseEntity<>(employeeInfoList, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeInfo> update(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateCommand command) {
        EmployeeInfo employeeInfo = employeeService.updateEmployee(id, command);
        return new ResponseEntity<>(employeeInfo, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByID(@PathVariable Long id) {
        employeeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
