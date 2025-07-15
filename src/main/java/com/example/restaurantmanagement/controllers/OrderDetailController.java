package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.infrastructure.dto.MenuItemStats;
import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.OrderDetail;
import com.example.restaurantmanagement.model.OrderDetail.OrderDetailId;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.OrderDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createOrderDetail(@RequestBody OrderDetail detail) {
        if (detail == null || detail.getOrder() == null || detail.getItem() == null
                || detail.getOrder().getId() == null || detail.getItem().getId() == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing orderId or itemId"});
        }
        OrderDetail created = orderDetailService.createOrderDetail(detail);
        return ResponseEntity.ok(new ResponseObject(created));
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllOrderDetails() {
        List<OrderDetail> details = orderDetailService.getAllOrderDetails();
        return ResponseEntity.ok(new ResponseObject(details));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseObject> getDetailsByOrderId(@PathVariable Integer orderId) {
        List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(orderId);
        return ResponseEntity.ok(new ResponseObject(details));
    }
    @GetMapping("/top-items-per-day")
    public ResponseEntity<ResponseObject> getTopItemsByDay(@RequestParam(defaultValue = "5") int topN) {
        Map<String, List<MenuItemStats>> result = orderDetailService.getTopMenuItemsPerDayOfWeek(topN);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Top items by day", result));
    }

    @GetMapping("/find")
    public ResponseEntity<ResponseObject> getDetailById(@RequestParam Integer orderId,
                                                        @RequestParam Integer itemId) {
        if (orderId == null || itemId == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing orderId or itemId"});
        }

        OrderDetailId id = new OrderDetailId(orderId, itemId);
        OrderDetail detail = orderDetailService.getOrderDetailById(id)
                .orElseThrow(() -> new NVException(ErrorCode.ORDER_DETAIL_NOT_FOUND));
        return ResponseEntity.ok(new ResponseObject(detail));
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateOrderDetail(@RequestBody OrderDetail detail) {
        if (detail == null || detail.getOrder() == null || detail.getItem() == null
                || detail.getOrder().getId() == null || detail.getItem().getId() == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing orderId or itemId"});
        }
        OrderDetail updated = orderDetailService.updateOrderDetail(detail);
        return ResponseEntity.ok(new ResponseObject(updated));
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteOrderDetail(@RequestBody Map<String, Integer> body) {
        Integer orderId = body.get("orderId");
        Integer itemId = body.get("itemId");

        if (orderId == null || itemId == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing orderId or itemId"});
        }

        OrderDetailId id = new OrderDetailId(orderId, itemId);
        if (orderDetailService.getOrderDetailById(id).isEmpty()) {
            throw new NVException(ErrorCode.ORDER_DETAIL_NOT_FOUND);
        }
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Order detail deleted successfully"));
    }
}
