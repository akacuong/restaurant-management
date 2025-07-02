package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.*;
import com.example.restaurantmanagement.repository.*;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    // CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createOrder(@RequestBody Order order) {
        try {
            Order created = orderService.createOrder(order);
            return ResponseEntity.ok(new ResponseObject(created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("CREATE_FAILED", e.getMessage()));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllOrders() {
        return ResponseEntity.ok(new ResponseObject(orderService.getAllOrders()));
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok(new ResponseObject(order)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Order not found")));
    }

    // UPDATE
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateOrder(@PathVariable Integer id, @RequestBody Order order) {
        try {
            order.setId(id);
            Order updated = orderService.updateOrder(order);
            return ResponseEntity.ok(new ResponseObject(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("UPDATE_FAILED", e.getMessage()));
        }
    }

    // DELETE
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteOrder(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");

        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Order deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", e.getMessage()));
        }
    }
}
