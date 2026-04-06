package com.example.restaurant.service;

import com.example.restaurant.dto.OrderItemCreateCommand;
import com.example.restaurant.dto.OrderItemInfo;
import com.example.restaurant.dto.OrderItemQuantityUpdateCommand;
import com.example.restaurant.exceptionhandling.OrderItemNotFoundException;
import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.OrderItem;
import com.example.restaurant.repository.OrderItemRepository;
import com.example.restaurant.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderItemRepository orderItemRepository;
    private final MenuService menuService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, MenuService menuService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.orderItemRepository = orderItemRepository;
        this.menuService = menuService;
    }

    public OrderItemInfo save(@Valid OrderItemCreateCommand command) {
        MenuItem menuItem = menuService.findById((long)command.getMenuItemId());
        OrderItem orderItemToSave = new  OrderItem();
        orderItemToSave.setMenuItem(menuItem);
        orderItemToSave.setQuantity(command.getQuantity());
        orderItemToSave.setPrice(menuItem.getPrice());
        orderItemRepository.save(orderItemToSave);
        OrderItemInfo orderItemInfo = modelMapper.map(orderItemToSave, OrderItemInfo.class);
        orderItemInfo.setName(menuItem.getName());
        return orderItemInfo;
    }

    public OrderItem findOrderItemById(Long id){
        return orderItemRepository.findById(id).orElseThrow(()->new OrderItemNotFoundException(id));
    }
    public OrderItemInfo updateOrderItemQuantity(Long id, @Valid OrderItemQuantityUpdateCommand command) {
        OrderItem orderItemToUpdate = findOrderItemById(id);
        orderItemToUpdate.setQuantity(command.getQuantity());
        OrderItem savedOrderItem = orderItemRepository.save(orderItemToUpdate);
        OrderItemInfo orderItemInfo = modelMapper.map(orderItemToUpdate, OrderItemInfo.class);
        orderItemInfo.setName(savedOrderItem.getMenuItem().getName());
        return orderItemInfo;
    }
}
