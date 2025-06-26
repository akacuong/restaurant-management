package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.OrderDetail;
import com.example.restaurantmanagement.model.OrderDetail.OrderDetailId;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.OrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    // ✅ CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createOrderDetail(@RequestBody OrderDetail detail) {
        try {
            OrderDetail created = orderDetailService.createOrderDetail(detail);
            return ResponseEntity.ok(new ResponseObject(created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("CREATE_FAILED", e.getMessage()));
        }
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllOrderDetails() {
        List<OrderDetail> details = orderDetailService.getAllOrderDetails();
        return ResponseEntity.ok(new ResponseObject(details));
    }

    // ✅ READ BY ORDER ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseObject> getDetailsByOrderId(@PathVariable Integer orderId) {
        List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(orderId);
        return ResponseEntity.ok(new ResponseObject(details));
    }

    // ✅ READ BY COMPOSITE KEY (dễ tìm kiếm hơn)
    @GetMapping("/find")
    public ResponseEntity<ResponseObject> getDetailById(@RequestParam Integer orderId, @RequestParam Integer itemId) {
        OrderDetailId id = new OrderDetailId(orderId, itemId);
        Optional<OrderDetail> detail = orderDetailService.getOrderDetailById(id);
        return detail.map(value -> ResponseEntity.ok(new ResponseObject(value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Order detail not found")));
    }

    // ✅ UPDATE
    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateOrderDetail(@RequestBody OrderDetail detail) {
        try {
            OrderDetail updated = orderDetailService.updateOrderDetail(detail);
            return ResponseEntity.ok(new ResponseObject(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("UPDATE_FAILED", e.getMessage()));
        }
    }

    // ✅ DELETE BY COMPOSITE KEY
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteOrderDetail(@RequestParam Integer orderId,
                                                            @RequestParam Integer itemId) {
        OrderDetailId id = new OrderDetailId(orderId, itemId);
        Optional<OrderDetail> detail = orderDetailService.getOrderDetailById(id);
        if (detail.isPresent()) {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Order detail deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("NOT_FOUND", "Order detail not found"));
    }
}
