package com.example.restaurant.service;

import com.example.restaurant.dto.*;
import com.example.restaurant.exceptionhandling.MenuItemNotAvailableException;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final MenuService menuService;
    private final RestaurantService restaurantService;

    public OrderService(OrderRepository orderRepository , MenuService menuService, RestaurantService restaurantService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.menuService = menuService;
        this.restaurantService = restaurantService;
    }

    private void validateNotPaid(Orders order) {
        if (order.getStatus() == OrderStatus.PAID) {
            throw new OrderAlreadyPaidException(order.getId());
        }
    }

    private Orders findById(Long id){
        return  orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException(id));
    }

    public OrderInfo saveOrder(@Valid OrderCreateCommand command) {
        RestaurantTable restaurantTable = restaurantService.findTableById(command.getRestaurantTableId());
        restaurantTable.setTableStatus(TableStatus.OCCUPIED);
        Orders orders = new Orders();
        orders.setRestaurantTable(restaurantTable);
        orders.setStatus(OrderStatus.NEW);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemCreateCommand itemCommand : command.getOrderItems()) {
            MenuItem menuItem = menuService.findById((long) itemCommand.getMenuItemId());
            BigDecimal price = menuItem.getPrice().multiply(BigDecimal.valueOf(itemCommand.getQuantity()));

            OrderItem orderItem = new OrderItem();
            if (!menuItem.isAvailable()) throw new MenuItemNotAvailableException((long) itemCommand.getMenuItemId());
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
        Orders baseOrder = findById(orderId);
        validateNotPaid(baseOrder);
        RestaurantTable table = baseOrder.getRestaurantTable();
        List<Orders> orders = orderRepository.findByRestaurantTableAndStatus(table, OrderStatus.SERVED);
        Map<Long, OrderItemInfo> mergedOrderItems = new HashMap<>();

        for (Orders order : orders) {
            order.setStatus(OrderStatus.PAID);
            order.setPaidAt(LocalDateTime.now());

            for (OrderItem item : order.getItems()) {
                Long menuItemId = item.getMenuItem().getId();
                if (mergedOrderItems.containsKey(menuItemId)) {
                    OrderItemInfo orderItemInfo = mergedOrderItems.get(menuItemId);
                    orderItemInfo.setQuantity(orderItemInfo.getQuantity() + item.getQuantity());
                    orderItemInfo.setPrice(orderItemInfo.getPrice().add(item.getPrice()));
                } else {
                    OrderItemInfo orderItemInfoNew = new OrderItemInfo();
                    orderItemInfoNew.setId(item.getId());
                    orderItemInfoNew.setQuantity(item.getQuantity());
                    orderItemInfoNew.setPrice(item.getPrice());
                    orderItemInfoNew.setOrderItemName(item.getMenuItem().getName());
                    mergedOrderItems.put(menuItemId, orderItemInfoNew);
                }
            }
        }

        List<OrderItemInfo> allItems = new ArrayList<>(mergedOrderItems.values());
        BigDecimal total = allItems.stream()
                .map(OrderItemInfo::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        table.setTableStatus(TableStatus.FREE);
        orderRepository.saveAll(orders);

        OrderCheckoutInfo orderCheckoutInfo = new OrderCheckoutInfo();
        orderCheckoutInfo.setRestaurantTableId(table.getId());
        orderCheckoutInfo.setTableNumber(table.getNumber());
        orderCheckoutInfo.setTotalPrice(total);
        orderCheckoutInfo.setItems(allItems);
        orderCheckoutInfo.setStatus(OrderStatus.PAID);
        orderCheckoutInfo.setPaidAt(LocalDateTime.now());
        orderCheckoutInfo.setId(orderId);
        return orderCheckoutInfo;
    }
}
