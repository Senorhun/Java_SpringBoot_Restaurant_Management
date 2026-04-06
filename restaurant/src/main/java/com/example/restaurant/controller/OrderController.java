package com.example.restaurant.controller;

import com.example.restaurant.dto.OrderItemCreateCommand;
import com.example.restaurant.dto.OrderItemInfo;
import com.example.restaurant.dto.OrderItemQuantityUpdateCommand;
import com.example.restaurant.model.OrderItem;
import com.example.restaurant.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orderItem")
    public ResponseEntity<OrderItemInfo> saveOrderItem(@Valid @RequestBody OrderItemCreateCommand command) {
        OrderItemInfo orderItemInfo = orderService.save(command);
        return new ResponseEntity<>(orderItemInfo, HttpStatus.CREATED);
    }
    @PatchMapping("/orderItem/{id}/quantity")
    public ResponseEntity<OrderItemInfo> updateOrderItemQuantity(@PathVariable("id") Long id, @Valid @RequestBody OrderItemQuantityUpdateCommand command){
        OrderItemInfo orderItemInfo = orderService.updateOrderItemQuantity(id, command);
        return new ResponseEntity<>(orderItemInfo, HttpStatus.OK);
    }

}
