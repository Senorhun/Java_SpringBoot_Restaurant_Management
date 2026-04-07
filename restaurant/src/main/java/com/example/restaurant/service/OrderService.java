package com.example.restaurant.service;

import com.example.restaurant.dto.*;
import com.example.restaurant.exceptionhandling.OrderAlreadyPaidException;
import com.example.restaurant.exceptionhandling.OrderItemNotFoundException;
import com.example.restaurant.exceptionhandling.OrderNotFoundException;
import com.example.restaurant.model.*;
import com.example.restaurant.repository.OrderItemRepository;
import com.example.restaurant.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        orderItemInfo.setOrderItemName(menuItem.getName());
        return orderItemInfo;
    }

    private void validateNotPaid(Orders order) {
        if (order.getStatus() == OrderStatus.PAID) {
            throw new OrderAlreadyPaidException(order.getId());
        }
    }
    public OrderItem findOrderItemById(Long id){
        return orderItemRepository.findById(id).orElseThrow(()->new OrderItemNotFoundException(id));
    }
    Orders findById(Long id){
        return  orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException(id));
    }
    public OrderItemInfo updateOrderItemQuantity(Long id, @Valid OrderItemQuantityUpdateCommand command) {
        OrderItem orderItemToUpdate = findOrderItemById(id);
        orderItemToUpdate.setQuantity(command.getQuantity());
        OrderItem savedOrderItem = orderItemRepository.save(orderItemToUpdate);
        OrderItemInfo orderItemInfo = modelMapper.map(orderItemToUpdate, OrderItemInfo.class);
        orderItemInfo.setOrderItemName(savedOrderItem.getMenuItem().getName());
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
        Orders savedOrder = orderRepository.save(orders);
        OrderInfo orderInfo = modelMapper.map(orders, OrderInfo.class);
        orderInfo.setTableNumber(savedOrder.getRestaurantTable().getNumber());
        orderInfo.setRestaurantTableId(savedOrder.getRestaurantTable().getId());
        List<OrderItemInfo> orderItemInfos = savedOrder.getItems().stream()
                .map(item -> {
                    OrderItemInfo itemInfo = modelMapper.map(item, OrderItemInfo.class);
                    itemInfo.setOrderItemName(item.getMenuItem().getName());
                    return itemInfo;
                }).toList();
        orderInfo.setItems(orderItemInfos);
        return orderInfo;
    }

    public void delete(Long id) {
        Orders orders = findById(id);
        validateNotPaid(orders);
        orderRepository.delete(orders);
    }

    public OrderUpdateStatusInfo updateOrderStatus(Long id, OrderStatusUpdateCommand command) {
        Orders order = findById(id);
        validateNotPaid(order);
        order.setStatus(command.getOrderStatus());
        orderRepository.save(order);
        OrderUpdateStatusInfo orderUpdateStatusInfo = modelMapper.map(order, OrderUpdateStatusInfo.class);
        orderUpdateStatusInfo.setOrderStatus(command.getOrderStatus());
        return orderUpdateStatusInfo;
    }

    public OrderCheckoutInfo checkout(Long orderId) {
        Orders order = findById(orderId);
        validateNotPaid(order);
        order.setStatus(OrderStatus.PAID);
        RestaurantTable table = order.getRestaurantTable();
        table.setTableStatus(TableStatus.FREE);
        order.setPaidAt(LocalDateTime.now());
        Orders savedOrder = orderRepository.save(order);

        OrderCheckoutInfo orderCheckoutInfo = modelMapper.map(savedOrder, OrderCheckoutInfo.class);
        orderCheckoutInfo.setTableNumber(savedOrder.getRestaurantTable().getNumber());
        orderCheckoutInfo.setRestaurantTableId(savedOrder.getRestaurantTable().getId());
        List<OrderItemInfo> items = savedOrder.getItems().stream()
                .map(item -> {
                    OrderItemInfo info = modelMapper.map(item, OrderItemInfo.class);
                    info.setOrderItemName(item.getMenuItem().getName());
                    return info;
                }).toList();

        orderCheckoutInfo.setItems(items);
        return orderCheckoutInfo;
    }
}
