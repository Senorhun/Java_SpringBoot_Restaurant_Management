package com.example.restaurant.service;

import com.example.restaurant.dto.*;
import com.example.restaurant.exceptionhandling.OrderItemNotFoundException;
import com.example.restaurant.model.*;
import com.example.restaurant.repository.OrderItemRepository;
import com.example.restaurant.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderItemRepository orderItemRepository;
    private final MenuService menuService;
    private final RestaurantService restaurantService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, MenuService menuService, RestaurantService restaurantService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.orderItemRepository = orderItemRepository;
        this.menuService = menuService;
        this.restaurantService = restaurantService;
    }

    public OrderItemInfo saveOrderItem(@Valid OrderItemCreateCommand command) {
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

    public OrderInfo saveOrder(@Valid OrderCreateCommand command) {
        RestaurantTable restaurantTable = restaurantService.findTableById(command.getRestaurantTableId());

        Orders orders = new Orders();
        orders.setRestaurantTable(restaurantTable);
        orders.setStatus(OrderStatus.NEW);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemCreateCommand itemCommand : command.getOrderItems()) {
            MenuItem menuItem = menuService.findById((long) itemCommand.getMenuItemId());
            BigDecimal price = menuItem.getPrice().multiply(BigDecimal.valueOf(itemCommand.getQuantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemCommand.getQuantity());
            orderItem.setPrice(price);
            orderItem.setOrders(orders);

            orderItems.add(orderItem);
            totalPrice = totalPrice.add(price);
        }

        orders.setItems(orderItems);
        orders.setTotalPrice(totalPrice);
        orderRepository.save(orders);
        return modelMapper.map(orders, OrderInfo.class);
    }
}
