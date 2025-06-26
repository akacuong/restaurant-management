package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.*;
import com.example.restaurantmanagement.repository.*;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.OrderService;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createOrder(@RequestBody Order order) {
        try {
            // Table
            if (order.getTable() == null || order.getTable().getId() == null) {
                return ResponseEntity.badRequest().body(new ResponseObject("VALIDATION_FAILED", "Table is required with valid ID"));
            }
            TableInfo table = tableInfoRepository.findById(order.getTable().getId())
                    .orElseThrow(() -> new RuntimeException("Table not found"));
            order.setTable(table);

            // Customer
            if (order.getCustomer() == null || order.getCustomer().getId() == null) {
                return ResponseEntity.badRequest().body(new ResponseObject("VALIDATION_FAILED", "Customer is required with valid ID"));
            }
            Customer customer = customerRepository.findById(order.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            order.setCustomer(customer);

            // Staff
            if (order.getStaff() == null || order.getStaff().getId() == null) {
                return ResponseEntity.badRequest().body(new ResponseObject("VALIDATION_FAILED", "Staff is required with valid ID"));
            }
            Staff staff = staffRepository.findById(order.getStaff().getId())
                    .orElseThrow(() -> new RuntimeException("Staff not found"));
            order.setStaff(staff);

            Order created = orderService.createOrder(order);
            return ResponseEntity.ok(new ResponseObject(created));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("CREATE_FAILED", e.getMessage()));
        }
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ResponseObject(orders));
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrderById(@PathVariable Integer id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(value -> ResponseEntity.ok(new ResponseObject(value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Order not found")));
    }

    // ✅ UPDATE
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateOrder(@PathVariable Integer id, @RequestBody Order updatedOrder) {
        Optional<Order> optional = orderService.getOrderById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", "Order not found"));
        }

        try {
            Order order = optional.get();
            order.setOrderTime(updatedOrder.getOrderTime());
            order.setStatus(updatedOrder.getStatus());
            order.setTotal(updatedOrder.getTotal());

            if (updatedOrder.getTable() != null && updatedOrder.getTable().getId() != null) {
                TableInfo table = tableInfoRepository.findById(updatedOrder.getTable().getId())
                        .orElseThrow(() -> new RuntimeException("Table not found"));
                order.setTable(table);
            }

            if (updatedOrder.getCustomer() != null && updatedOrder.getCustomer().getId() != null) {
                Customer customer = customerRepository.findById(updatedOrder.getCustomer().getId())
                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                order.setCustomer(customer);
            }

            if (updatedOrder.getStaff() != null && updatedOrder.getStaff().getId() != null) {
                Staff staff = staffRepository.findById(updatedOrder.getStaff().getId())
                        .orElseThrow(() -> new RuntimeException("Staff not found"));
                order.setStaff(staff);
            }

            Order updated = orderService.updateOrder(order);
            return ResponseEntity.ok(new ResponseObject(updated));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("UPDATE_FAILED", e.getMessage()));
        }
    }

    // ✅ DELETE
    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteOrder(@PathVariable Integer id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", "Order not found"));
        }
        orderService.deleteOrder(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Order deleted successfully"));
    }
}
