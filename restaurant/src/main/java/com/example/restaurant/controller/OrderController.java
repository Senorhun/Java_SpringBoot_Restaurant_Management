package com.example.restaurant.controller;

import com.example.restaurant.dto.*;
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

    @PostMapping
    public ResponseEntity<OrderInfo> saveOrder(@Valid @RequestBody OrderCreateCommand command){
        OrderInfo orderInfo = orderService.saveOrder(command);
        return new ResponseEntity<>(orderInfo, HttpStatus.CREATED);
    }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id){
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PatchMapping("/{id}/orderStatus")
    public ResponseEntity<OrderUpdateStatusInfo> updateOrderStatus(@PathVariable("id") Long id, @Valid @RequestBody OrderStatusUpdateCommand command){
        OrderUpdateStatusInfo orderUpdateStatusInfo = orderService.updateOrderStatus(id, command);
        return new ResponseEntity<>(orderUpdateStatusInfo, HttpStatus.OK);
    }
    @GetMapping("/{id}/checkout")
    public ResponseEntity<OrderCheckoutInfo> checkout(@PathVariable("id") Long orderId){
        OrderCheckoutInfo orderCheckoutInfoInfo = orderService.checkout(orderId);
        return new ResponseEntity<>(orderCheckoutInfoInfo, HttpStatus.OK);
    }

}
