package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.OrderDetail;
import com.example.restaurantmanagement.model.OrderDetail.OrderDetailId;
import com.example.restaurantmanagement.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @Autowired
    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<OrderDetail> createOrderDetail(@RequestBody OrderDetail detail) {
        return ResponseEntity.ok(orderDetailService.saveOrderDetail(detail)); // ✅ tên đúng
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<OrderDetail>> getAll() {
        return ResponseEntity.ok(orderDetailService.getAllOrderDetails()); // ✅ tên đúng
    }

    // ✅ GET BY ORDER ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetail>> getByOrderId(@PathVariable Integer orderId) {
        List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(orderId); // ✅ đúng method
        return ResponseEntity.ok(details);
    }

    // ✅ DELETE BY COMPOSITE KEY
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> delete(@PathVariable Integer orderId, @PathVariable Integer itemId) {
        OrderDetailId id = new OrderDetailId(orderId, itemId);
        Optional<OrderDetail> detail = orderDetailService.getOrderDetailById(id); // ✅ đúng method
        if (detail.isPresent()) {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok("Deleted successfully.");
        }
        return ResponseEntity.status(404).body("Order detail not found.");
    }
}
