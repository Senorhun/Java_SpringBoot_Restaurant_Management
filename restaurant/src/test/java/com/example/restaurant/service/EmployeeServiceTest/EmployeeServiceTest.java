package com.example.restaurant.service.EmployeeServiceTest;

import com.example.restaurant.dto.EmployeeCreateCommand;
import com.example.restaurant.dto.EmployeeInfo;
import com.example.restaurant.exceptionhandling.EmployeeEmailDuplicateException;
import com.example.restaurant.exceptionhandling.EmployeeNicknameDuplicateException;
import com.example.restaurant.exceptionhandling.EmployeeNotFoundException;
import com.example.restaurant.model.Employee;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.repository.EmployeeRepository;
import com.example.restaurant.service.EmployeeService;
import com.example.restaurant.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private EmployeeService employeeService;


    @Test
    void getEmployeeById_shouldReturnEmployeeInfo_whenEmployeeExists() {
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");
        employee.setRestaurant(restaurant);

        EmployeeInfo employeeInfo = new EmployeeInfo();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee, EmployeeInfo.class)).thenReturn(employeeInfo);
        EmployeeInfo result = employeeService.getEmployeeById(id);

        assertNotNull(employeeInfo);
        assertEquals("BurgerGo", result.getRestaurantName());
    }
    @Test
    void getEmployeeById_shouldThrowException_whenEmployeeDoesNotExist() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(employeeId));
    }
    @Test
    void save_shouldThrowException_whenNicknameExists() {
        EmployeeCreateCommand command = new EmployeeCreateCommand();
        command.setNickname("Bono");
        when(employeeRepository.existsByNicknameIgnoreCase("Bono")).thenReturn(true);
        assertThrows(EmployeeNicknameDuplicateException.class,() -> employeeService.save(command));
    }
    @Test
    void save_shouldThrowException_whenEmailExists() {
        EmployeeCreateCommand command = new EmployeeCreateCommand();
        command.setEmail("bono@gmail.com");
        when(employeeRepository.existsByEmailIgnoreCase("bono@gmail.com")).thenReturn(true);
        assertThrows(EmployeeEmailDuplicateException.class, ()->  employeeService.save(command));
    }
    @Test
    void delete_shouldCallRepository_whenEmployeeExists() {
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        employeeService.delete(id);
        verify(employeeRepository).delete(employee);
    }



}
