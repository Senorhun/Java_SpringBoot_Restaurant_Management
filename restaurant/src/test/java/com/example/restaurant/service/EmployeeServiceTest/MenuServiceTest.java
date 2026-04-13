package com.example.restaurant.service.EmployeeServiceTest;

import com.example.restaurant.dto.*;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.MenuItemType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    @Test
    void getByType_shouldReturnPagedMenuItemInfo() {
        MenuItemType type = MenuItemType.DESSERT;
        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");

        MenuItem menuItem1 = new MenuItem();
        menuItem1.setRestaurant(restaurant);
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setRestaurant(restaurant);

        List<MenuItem> items = new ArrayList<>();
        items.add(menuItem1);
        items.add(menuItem2);

        Page<MenuItem> page = new PageImpl<>(items);

        MenuItemInfo menuItemInfo1 = new MenuItemInfo();
        MenuItemInfo menuItemInfo2 = new MenuItemInfo();

        when(menuRepository.findByMenuItemTypeAndAvailableTrue(eq(MenuItemType.DESSERT), any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(menuItem1, MenuItemInfo.class)).thenReturn(menuItemInfo1);
        when(modelMapper.map(menuItem2, MenuItemInfo.class)).thenReturn(menuItemInfo2);

        Page<MenuItemInfo> result = menuService.getByType(type, 0, 10);
        assertEquals(2, result.getContent().size());
        assertEquals("BurgerGo", result.getContent().getFirst().getRestaurantName());

        verify(menuRepository).findByMenuItemTypeAndAvailableTrue(eq(type), any(Pageable.class));
    }
    @Test
    void getAvailableMenuItems_shouldReturnMenuItemInfo_whenMenuExists() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");

        MenuItem menuItem1 = new MenuItem();
        menuItem1.setRestaurant(restaurant);
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setRestaurant(restaurant);

        List<MenuItem> items = List.of(menuItem1, menuItem2);
        Page<MenuItem> page = new PageImpl<>(items);
        MenuItemInfo menuItemInfo1 = new MenuItemInfo();
        MenuItemInfo menuItemInfo2 = new MenuItemInfo();

        when(menuRepository.findByAvailableTrue(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(menuItem1, MenuItemInfo.class)).thenReturn(menuItemInfo1);
        when(modelMapper.map(menuItem2, MenuItemInfo.class)).thenReturn(menuItemInfo2);

        Page<MenuItemInfo> result = menuService.getAvailableMenuItems(0, 10);

        assertEquals(2, result.getContent().size());
        assertEquals("BurgerGo", result.getContent().getFirst().getRestaurantName());
    }
    @Test
    void updateMenuItem_shouldUpdateMenuItem_whenMenuExists() {
        Long id = 1L;
        MenuItem menuItemToUpdate = new MenuItem();
        menuItemToUpdate.setId(id);

        MenuItemUpdateCommand command = new MenuItemUpdateCommand();
        command.setRestaurantId(10L);

        MenuItemInfo menuItemInfo = new MenuItemInfo();

        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");


        when(menuRepository.findById(id)).thenReturn(Optional.of(menuItemToUpdate));
        doNothing().when(modelMapper).map(command, menuItemToUpdate);
        when(modelMapper.map(menuItemToUpdate, MenuItemInfo.class)).thenReturn(menuItemInfo);
        when(restaurantService.findById(command.getRestaurantId())).thenReturn(restaurant);

        MenuItemInfo result =  menuService.updateMenuItem(id, command);
        assertNotNull(result);
        assertEquals("BurgerGo", result.getRestaurantName());


    }
    @Test
    void deleteMenuItem_shouldDeleteMenuItem_whenMenuExists() {
        Long id = 1L;
        MenuItem menuItemToDelete = new MenuItem();
        menuItemToDelete.setId(id);

        when(menuRepository.findById(id)).thenReturn(Optional.of(menuItemToDelete));
        menuService.deleteById(id);
        verify(menuRepository).delete(menuItemToDelete);
    }
    @Test
    void updateMenuItemAvailability_shouldUpdateMenuItemAvailability_whenMenuExists() {
        Long id = 1L;
        MenuItem menuItemToUpdate = new MenuItem();
        menuItemToUpdate.setId(id);

        Restaurant restaurant = new Restaurant();
        restaurant.setName("BurgerGo");
        menuItemToUpdate.setRestaurant(restaurant);

        MenuItemUpdateAvailabilityCommand command = new MenuItemUpdateAvailabilityCommand();
        command.setAvailable(true);
        MenuItemInfo menuItemInfo = new MenuItemInfo();

        when(menuRepository.findById(id)).thenReturn(Optional.of(menuItemToUpdate));
        when(menuRepository.save(menuItemToUpdate)).thenReturn(menuItemToUpdate);
        when(modelMapper.map(menuItemToUpdate, MenuItemInfo.class)).thenReturn(menuItemInfo);

        MenuItemInfo result = menuService.updateAvailability(id, command);
        assertNotNull(result);
        assertTrue(menuItemToUpdate.isAvailable());
        assertEquals("BurgerGo", result.getRestaurantName());
    }
}
