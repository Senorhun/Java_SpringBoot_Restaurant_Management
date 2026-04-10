package com.example.restaurant.service.EmployeeServiceTest;

import com.example.restaurant.dto.MenuItemCreateCommand;
import com.example.restaurant.dto.MenuItemInfo;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.repository.MenuRepository;
import com.example.restaurant.service.MenuService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    ModelMapper modelMapper;

    @Mock
    MenuRepository menuRepository;

    @Mock
    RestaurantService restaurantService;

    @InjectMocks
    MenuService menuService;

    @Test
    void save_shouldReturnMenuInfo_whenMenuExists(){
        MenuItemCreateCommand command = new MenuItemCreateCommand();
        Long restaurantId = 1L;
        command.setRestaurantId(restaurantId);
        command.setName("Burger");

        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");

        MenuItem menuItem = new MenuItem();
        MenuItem savedMenuItem = new MenuItem();
        MenuItemInfo menuItemInfo = new MenuItemInfo();

        when(menuRepository.existsByNameIgnoreCase("Burger")).thenReturn(false);
        when(restaurantService.findById(restaurantId)).thenReturn(restaurant);
        when(modelMapper.map(command, MenuItem.class)).thenReturn(menuItem);
        when(menuRepository.save(menuItem)).thenReturn(savedMenuItem);
        when(modelMapper.map(savedMenuItem, MenuItemInfo.class)).thenReturn(menuItemInfo);

        MenuItemInfo result = menuService.save(command);
        assertNotNull(result);
        assertEquals("BurgerGo", result.getRestaurantName());

    }

    @Test
    void getMenuItemById_shouldReturnMenuInfo_whenMenuExists() {
        Long id = 1L;
        MenuItem menuItem = new MenuItem();
        menuItem.setId(id);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");
        menuItem.setRestaurant(restaurant);
        MenuItemInfo menuItemInfo = new MenuItemInfo();
        when(menuRepository.findById(id)).thenReturn(Optional.of(menuItem));
        when(modelMapper.map(menuItem, MenuItemInfo.class)).thenReturn(menuItemInfo);
        MenuItemInfo result = menuService.getMenuItemById(id);
        assertNotNull(result);
        assertEquals("BurgerGo", result.getRestaurantName());
    }
    @Test
    void findAll_shouldReturnMenuInfoList_whenMenuExists() {
        MenuItem menuItem1 = new MenuItem();
        MenuItem menuItem2 = new MenuItem();
        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");
        menuItem1.setRestaurant(restaurant);
        menuItem2.setRestaurant(restaurant);
        List<MenuItem> menuItemList = new ArrayList<>();
        menuItemList.add(menuItem1);
        menuItemList.add(menuItem2);
        MenuItemInfo menuItemInfo1 = new MenuItemInfo();
        MenuItemInfo menuItemInfo2 = new MenuItemInfo();

        when(menuRepository.findAll()).thenReturn(menuItemList);
        when(modelMapper.map(menuItem1, MenuItemInfo.class)).thenReturn(menuItemInfo1);
        when(modelMapper.map(menuItem2, MenuItemInfo.class)).thenReturn(menuItemInfo2);
        List<MenuItemInfo> result = menuService.findAll();

        assertNotNull(result);
        assertEquals("BurgerGo", result.getFirst().getRestaurantName());
        assertEquals(2, result.size());
    }
}
