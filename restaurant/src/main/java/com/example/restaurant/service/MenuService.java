package com.example.restaurant.service;

import com.example.restaurant.dto.MenuItemCreateCommand;
import com.example.restaurant.dto.MenuItemInfo;
import com.example.restaurant.dto.MenuItemUpdateAvailabilityCommand;
import com.example.restaurant.dto.MenuItemUpdateCommand;
import com.example.restaurant.exceptionhandling.MenuItemNotFoundException;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.MenuItemType;
import com.example.restaurant.repository.MenuRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    public MenuService(MenuRepository menuRepository, ModelMapper modelMapper) {
        this.menuRepository = menuRepository;
        this.modelMapper = modelMapper;

    }

    public MenuItemInfo save(@Valid MenuItemCreateCommand command) {
        MenuItem menuItemToSave = modelMapper.map(command, MenuItem.class);
        menuRepository.save(menuItemToSave);
        return modelMapper.map(menuItemToSave, MenuItemInfo.class);
    }

    public List<MenuItemInfo> findAll() {
        List<MenuItem> menuItems = menuRepository.findAll();
        return menuItems.stream().map(menuItem-> modelMapper.map(menuItem,MenuItemInfo.class)).toList();
    }

    public MenuItem findById(Long id){
        return menuRepository.findById(id).orElseThrow(() -> new MenuItemNotFoundException(id));
    }
    public MenuItemInfo getMenuItemById(Long id) {
        MenuItem menuItem = findById(id);
        return modelMapper.map(menuItem,MenuItemInfo.class);
    }
    public List<MenuItemInfo> getByType(MenuItemType type) {
        List<MenuItem> menuItems = menuRepository.getByType(type);
        return menuItems.stream().map(m -> modelMapper.map(m,MenuItemInfo.class)).toList();
    }

    public MenuItemInfo updateMenuItem(Long id, @Valid MenuItemUpdateCommand command) {
        MenuItem menuItemToUpdate = findById(id);
        modelMapper.map(command, menuItemToUpdate);
        menuRepository.save(menuItemToUpdate);
        return modelMapper.map(menuItemToUpdate, MenuItemInfo.class);
    }

    public void deleteById(Long id) {
        MenuItem menuItemToDelete = findById(id);
        menuRepository.delete(menuItemToDelete);
    }

    public List<MenuItemInfo> findAllAvailable() {
        List<MenuItem> menuItems = menuRepository.findAll();
        return menuItems.stream().filter(MenuItem::isAvailable)
                .map(m -> modelMapper.map(m,MenuItemInfo.class)).toList();
    }

    public MenuItemInfo updateAvailability(Long id, MenuItemUpdateAvailabilityCommand command) {
        MenuItem menuItemToUpdate = findById(id);
        menuItemToUpdate.setAvailable(command.isAvailable());
        return modelMapper.map(menuItemToUpdate, MenuItemInfo.class);
    }
}
