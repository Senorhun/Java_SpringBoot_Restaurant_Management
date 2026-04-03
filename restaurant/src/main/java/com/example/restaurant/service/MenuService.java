package com.example.restaurant.service;

import com.example.restaurant.dto.MenuItemCreateCommand;
import com.example.restaurant.dto.MenuItemInfo;
import com.example.restaurant.model.MenuItem;
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
}
