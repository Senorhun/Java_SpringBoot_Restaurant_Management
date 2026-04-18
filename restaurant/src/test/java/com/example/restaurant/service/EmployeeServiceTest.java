package com.example.restaurant.service;

import com.example.restaurant.dto.EmployeeCreateCommand;
import com.example.restaurant.dto.EmployeeInfo;
import com.example.restaurant.dto.EmployeeUpdateCommand;
import com.example.restaurant.exceptionhandling.EmployeeEmailDuplicateException;
import com.example.restaurant.exceptionhandling.EmployeeNicknameDuplicateException;
import com.example.restaurant.exceptionhandling.EmployeeNotFoundException;
import com.example.restaurant.model.Employee;
import com.example.restaurant.model.EmployeeType;
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

import java.util.ArrayList;
import java.util.List;
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

        assertNotNull(result);
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
    @Test
    void findAll_shouldReturnListOfEmployeeInfo_whenEmployeeExists() {
        Long id1 = 1L;
        Long id2 = 2L;

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("BurgerGo");
        Employee employee1 = new Employee();
        employee1.setId(id1);
        employee1.setRestaurant(restaurant1);

        Employee employee2 = new Employee();
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("PizzaGo");
        employee2.setId(id2);
        employee2.setRestaurant(restaurant2);

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        EmployeeInfo employeeInfo1 = new EmployeeInfo();
        EmployeeInfo employeeInfo2 = new EmployeeInfo();

        when(employeeRepository.findAll()).thenReturn(employees);
        when(modelMapper.map(employee1, EmployeeInfo.class)).thenReturn(employeeInfo1);
        when(modelMapper.map(employee2, EmployeeInfo.class)).thenReturn(employeeInfo2);
        List<EmployeeInfo> employeeInfos = employeeService.findAll();

        assertEquals(2, employeeInfos.size());
        assertTrue(employeeInfos.contains(employeeInfo1));
        assertTrue(employeeInfos.contains(employeeInfo2));
        assertEquals("BurgerGo", employeeInfo1.getRestaurantName());
        assertEquals("PizzaGo", employeeInfo2.getRestaurantName());
    }

    @Test
    void updateEmployee_shouldReturnEmployeeInfo_whenEmployeeExists(){
        Long employeeId = 1L;
        Employee employeeToUpdate = new Employee();
        employeeToUpdate.setId(employeeId);

        EmployeeUpdateCommand command = new EmployeeUpdateCommand();
        command.setRestaurantId(10L);

        EmployeeInfo employeeInfo = new EmployeeInfo();

        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employeeToUpdate));
        doNothing().when(modelMapper).map(command, employeeToUpdate);
        when(modelMapper.map(employeeToUpdate, EmployeeInfo.class)).thenReturn(employeeInfo);
        when(restaurantService.findById(command.getRestaurantId())).thenReturn(restaurant);

        EmployeeInfo result = employeeService.updateEmployee(employeeId, command);
        assertNotNull(result);
        assertEquals("BurgerGo", result.getRestaurantName());
    }
    @Test
    void getByType_shouldReturnListOfEmployeeInfo_whenEmployeeExists() {
        Employee employee1 = new Employee();
        employee1.setEmployeeType(EmployeeType.WAITER);
        Restaurant restaurant =  new Restaurant();
        restaurant.setName("BurgerGo");
        employee1.setRestaurant(restaurant);

        Employee employee2 = new Employee();
        employee2.setEmployeeType(EmployeeType.WAITER);
        employee2.setRestaurant(restaurant);

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        EmployeeInfo employeeInfo1 = new EmployeeInfo();
        EmployeeInfo employeeInfo2 = new EmployeeInfo();
        when(employeeRepository.getByType(EmployeeType.WAITER)).thenReturn(employees);
        when(modelMapper.map(employee1,EmployeeInfo.class)).thenReturn(employeeInfo1);
        when(modelMapper.map(employee2,EmployeeInfo.class)).thenReturn(employeeInfo2);
        List<EmployeeInfo> result = employeeService.getByType(EmployeeType.WAITER);
        assertEquals(2,result.size());
        assertEquals("BurgerGo", result.get(0).getRestaurantName());
    }
    @Test
    void getAll_shouldReturnListOfEmployeeInfo_whenEmployeeExists() {
        Employee employee1 = new Employee();
        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");
        employee1.setRestaurant(restaurant);
        Employee employee2 = new Employee();
        employee2.setRestaurant(restaurant);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        EmployeeInfo employeeInfo1 = new EmployeeInfo();
        EmployeeInfo employeeInfo2 = new EmployeeInfo();
        when(employeeRepository.findAll()).thenReturn(employees);
        when(modelMapper.map(employee1,EmployeeInfo.class)).thenReturn(employeeInfo1);
        when(modelMapper.map(employee2,EmployeeInfo.class)).thenReturn(employeeInfo2);
        List<EmployeeInfo> result = employeeService.findAll();

        assertEquals(2,result.size());
        assertEquals("BurgerGo", result.get(0).getRestaurantName());
    }

}
