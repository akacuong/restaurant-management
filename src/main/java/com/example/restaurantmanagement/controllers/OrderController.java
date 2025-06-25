package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.*;
import com.example.restaurantmanagement.repository.*;
import com.example.restaurantmanagement.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final TableInfoRepository tableInfoRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public OrderController(OrderService orderService,
                           TableInfoRepository tableInfoRepository,
                           CustomerRepository customerRepository,
                           StaffRepository staffRepository) {
        this.orderService = orderService;
        this.tableInfoRepository = tableInfoRepository;
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            // Table
            if (order.getTable() != null && order.getTable().getTableId() != null) {
                TableInfo table = tableInfoRepository.findById(order.getTable().getTableId())
                        .orElseThrow(() -> new RuntimeException("Table not found"));
                order.setTable(table);
            } else {
                return ResponseEntity.badRequest().body("Table is required with valid ID.");
            }

            // Customer
            if (order.getCustomer() != null && order.getCustomer().getCustomerId() != null) {
                Customer customer = customerRepository.findById(order.getCustomer().getCustomerId())
                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                order.setCustomer(customer);
            } else {
                return ResponseEntity.badRequest().body("Customer is required with valid ID.");
            }

            // Staff
            if (order.getStaff() != null && order.getStaff().getId() != null) {
                Staff staff = staffRepository.findById(order.getStaff().getId())
                        .orElseThrow(() -> new RuntimeException("Staff not found"));
                order.setStaff(staff);
            } else {
                return ResponseEntity.badRequest().body("Staff is required with valid ID.");
            }

            return ResponseEntity.ok(orderService.saveOrder(order));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer id) {
        Optional<Order> optional = orderService.getOrderById(id);
        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer id, @RequestBody Order updatedOrder) {
        Optional<Order> optional = orderService.getOrderById(id);
        if (optional.isPresent()) {
            Order order = optional.get();

            order.setOrderTime(updatedOrder.getOrderTime());
            order.setStatus(updatedOrder.getStatus());
            order.setTotal(updatedOrder.getTotal());
            if (updatedOrder.getTable() != null && updatedOrder.getTable().getTableId() != null) {
                TableInfo table = tableInfoRepository.findById(updatedOrder.getTable().getTableId())
                        .orElseThrow(() -> new RuntimeException("Table not found"));
                order.setTable(table);
            }

            if (updatedOrder.getCustomer() != null && updatedOrder.getCustomer().getCustomerId() != null) {
                Customer customer = customerRepository.findById(updatedOrder.getCustomer().getCustomerId())
                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                order.setCustomer(customer);
            }

            if (updatedOrder.getStaff() != null && updatedOrder.getStaff().getId() != null) {
                Staff staff = staffRepository.findById(updatedOrder.getStaff().getId())
                        .orElseThrow(() -> new RuntimeException("Staff not found"));
                order.setStaff(staff);
            }

            return ResponseEntity.ok(orderService.saveOrder(order));
        }
        return ResponseEntity.status(404).body("Order not found");
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.status(404).body("Order not found");
    }
}
