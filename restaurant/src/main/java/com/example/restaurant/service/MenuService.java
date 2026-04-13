package com.example.restaurant.service;

import com.example.restaurant.dto.MenuItemCreateCommand;
import com.example.restaurant.dto.MenuItemInfo;
import com.example.restaurant.dto.MenuItemUpdateAvailabilityCommand;
import com.example.restaurant.dto.MenuItemUpdateCommand;
import com.example.restaurant.exceptionhandling.MenuItemNameDuplicationException;
import com.example.restaurant.exceptionhandling.MenuItemNotFoundException;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.MenuItemType;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.repository.MenuRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;
    private final RestaurantService restaurantService;

    public MenuService(MenuRepository menuRepository, RestaurantService restaurantService, ModelMapper modelMapper) {
        this.menuRepository = menuRepository;
        this.modelMapper = modelMapper;
        this.restaurantService = restaurantService;
    }

    public MenuItemInfo save(@Valid MenuItemCreateCommand command) {
        if (menuRepository.existsByNameIgnoreCase(command.getName())) {
            throw new MenuItemNameDuplicationException(command.getName());
        }
        Restaurant restaurant = restaurantService.findById(command.getRestaurantId());
        MenuItem menuItemToSave = modelMapper.map(command, MenuItem.class);
        menuItemToSave.setRestaurant(restaurant);
        MenuItem savedMenuItem = menuRepository.save(menuItemToSave);
        MenuItemInfo menuItemInfo = modelMapper.map(savedMenuItem, MenuItemInfo.class);
        menuItemInfo.setRestaurantName(restaurant.getName());
        return menuItemInfo;
    }

    public List<MenuItemInfo> findAll() {
        List<MenuItem> menuItems = menuRepository.findAll();
        return menuItems.stream().map(menuItem-> {
            MenuItemInfo menuItemInfo = modelMapper.map(menuItem,MenuItemInfo.class);
            menuItemInfo.setRestaurantName(menuItem.getRestaurant().getName());
            return menuItemInfo;
        }).toList();
    }
    public Page<MenuItemInfo> getByType(MenuItemType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        return menuRepository.findByMenuItemTypeAndAvailableTrue(type, pageable)
                .map(menuItem -> {
                    MenuItemInfo dto = modelMapper.map(menuItem, MenuItemInfo.class);
                    dto.setRestaurantName(menuItem.getRestaurant().getName());
                    return dto;
                });
    }
    public Page<MenuItemInfo> getAvailableMenuItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<MenuItem> menuItemsPage = menuRepository.findByAvailableTrue(pageable);

        return menuItemsPage.map(menuItem -> {
            MenuItemInfo info = modelMapper.map(menuItem, MenuItemInfo.class);
            info.setRestaurantName(menuItem.getRestaurant().getName());
            return info;
        });
    }
    public MenuItem findById(Long id){
        return menuRepository.findById(id).orElseThrow(() -> new MenuItemNotFoundException(id));
    }
    public MenuItemInfo getMenuItemById(Long id) {
        MenuItem menuItem = findById(id);
        MenuItemInfo menuItemInfo = modelMapper.map(menuItem, MenuItemInfo.class);
        menuItemInfo.setRestaurantName(menuItem.getRestaurant().getName());
        return menuItemInfo;
    }

    public MenuItemInfo updateMenuItem(Long id, @Valid MenuItemUpdateCommand command) {
        MenuItem menuItemToUpdate = findById(id);
        modelMapper.map(command, menuItemToUpdate);
        Restaurant restaurant = restaurantService.findById(command.getRestaurantId());
        MenuItemInfo menuItemInfo = modelMapper.map(menuItemToUpdate, MenuItemInfo.class);
        menuItemInfo.setRestaurantName(restaurant.getName());
        menuRepository.save(menuItemToUpdate);
        return menuItemInfo;
    }

    public void deleteById(Long id) {
        MenuItem menuItemToDelete = findById(id);
        menuRepository.delete(menuItemToDelete);
    }

    public MenuItemInfo updateAvailability(Long id, @Valid MenuItemUpdateAvailabilityCommand command) {
        MenuItem menuItemToUpdate = findById(id);
        menuItemToUpdate.setAvailable(command.isAvailable());
        MenuItem savedMenuItem = menuRepository.save(menuItemToUpdate);
        MenuItemInfo menuItemInfo = modelMapper.map(savedMenuItem, MenuItemInfo.class);
        menuItemInfo.setRestaurantName(savedMenuItem.getRestaurant().getName());
        return menuItemInfo;
    }
}
