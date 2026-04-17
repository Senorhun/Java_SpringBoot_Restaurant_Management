package com.example.restaurant.service;

import com.example.restaurant.dto.*;
import com.example.restaurant.exceptionhandling.MenuItemNotAvailableException;
import com.example.restaurant.exceptionhandling.OrderAlreadyPaidException;
import com.example.restaurant.model.*;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.service.MenuService;
import com.example.restaurant.service.OrderService;
import com.example.restaurant.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuService menuService;
    @Mock
    private RestaurantService restaurantService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private OrderService orderService;

    @Test
    void saveOrder_shouldReturnOrderInfo_whenValid() {
        OrderCreateCommand command = new OrderCreateCommand();

        OrderItemCreateCommand itemCommand = new OrderItemCreateCommand();
        itemCommand.setMenuItemId(1);
        itemCommand.setQuantity(2);

        command.setOrderItems(List.of(itemCommand));
        command.setRestaurantTableId(1L);

        RestaurantTable table = new RestaurantTable();
        table.setId(1L);
        table.setNumber(10);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Burger");
        menuItem.setPrice(BigDecimal.valueOf(1000));
        menuItem.setAvailable(true);

        OrderInfo orderInfo = new OrderInfo();
        OrderItemInfo itemInfo = new OrderItemInfo();

        when(restaurantService.findTableById(1L)).thenReturn(table);
        when(menuService.findById(1L)).thenReturn(menuItem);
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(modelMapper.map(any(Orders.class), eq(OrderInfo.class))).thenReturn(orderInfo);
        when(modelMapper.map(any(OrderItem.class), eq(OrderItemInfo.class))).thenReturn(itemInfo);

        OrderInfo result = orderService.saveOrder(command);
        assertNotNull(result);
        verify(orderRepository).save(any());
    }
    @Test
    void saveOrder_shouldThrowException_whenMenuNotAvailable() {
        OrderCreateCommand command = new OrderCreateCommand();

        OrderItemCreateCommand item = new OrderItemCreateCommand();
        item.setMenuItemId(1);
        item.setQuantity(1);

        command.setOrderItems(List.of(item));
        command.setRestaurantTableId(1L);
        RestaurantTable table = new RestaurantTable();
        MenuItem menuItem = new MenuItem();
        menuItem.setAvailable(false);
        menuItem.setPrice(BigDecimal.valueOf(1000));

        when(restaurantService.findTableById(1L)).thenReturn(table);
        when(menuService.findById(1L)).thenReturn(menuItem);

        assertThrows(MenuItemNotAvailableException.class,() -> orderService.saveOrder(command));
    }
    @Test
    void delete_shouldCallRepository_whenNotPaid() {
        Orders order = new Orders();
        order.setStatus(OrderStatus.NEW);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        orderService.delete(1L);
        verify(orderRepository).delete(order);
    }
    @Test
    void delete_shouldThrowException_whenPaid() {
        Orders order = new Orders();
        order.setStatus(OrderStatus.PAID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(OrderAlreadyPaidException.class,() -> orderService.delete(1L));
    }
    @Test
    void updateOrderStatus_shouldUpdateStatus() {
        Orders order = new Orders();
        order.setStatus(OrderStatus.NEW);
        OrderStatusUpdateCommand command = new OrderStatusUpdateCommand();
        command.setOrderStatus(OrderStatus.SERVED);
        OrderUpdateStatusInfo info = new OrderUpdateStatusInfo();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderUpdateStatusInfo.class)).thenReturn(info);
        OrderUpdateStatusInfo result = orderService.updateOrderStatus(1L, command);
        assertEquals(OrderStatus.SERVED, result.getOrderStatus());
    }
    @Test
    void checkout_shouldReturnCheckoutInfo() {
        Orders baseOrder = new Orders();
        baseOrder.setStatus(OrderStatus.NEW);
        RestaurantTable table = new RestaurantTable();
        table.setId(1L);
        table.setNumber(5);
        baseOrder.setRestaurantTable(table);
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Burger");
        OrderItem item = new OrderItem();
        item.setMenuItem(menuItem);
        item.setQuantity(2);
        item.setPrice(BigDecimal.valueOf(2000));
        Orders servedOrder = new Orders();
        servedOrder.setItems(List.of(item));
        servedOrder.setRestaurantTable(table);
        servedOrder.setStatus(OrderStatus.SERVED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(baseOrder));
        when(orderRepository.findByRestaurantTableAndStatus(table, OrderStatus.SERVED)).thenReturn(List.of(servedOrder));
        OrderCheckoutInfo result = orderService.checkout(1L);
        assertNotNull(result);
        assertEquals(OrderStatus.PAID, result.getStatus());
    }

}