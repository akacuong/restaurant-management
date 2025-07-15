package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Order;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        if (order == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Order data is missing"});
        }
        Order created = orderService.createOrder(order);
        return ResponseEntity.ok(new ResponseObject(created));
    }
    // READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllOrders() {
        List<Order> list = orderService.getAllOrders();
        return ResponseEntity.ok(new ResponseObject(list));
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrderById(@PathVariable Integer id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new NVException(ErrorCode.ORDER_NOT_FOUND));
        return ResponseEntity.ok(new ResponseObject(order));
    }

    // UPDATE
    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateOrder(@RequestBody Integer id, @RequestBody Order order) {
        if (order == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Order data is missing"});
        }
        order.setId(id);
        Order updated = orderService.updateOrder(order);
        return ResponseEntity.ok(new ResponseObject(updated));
    }

    // DELETE
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteOrder(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        if (id == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing order ID"});
        }
        orderService.deleteOrder(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Order deleted successfully"));
    }
}
