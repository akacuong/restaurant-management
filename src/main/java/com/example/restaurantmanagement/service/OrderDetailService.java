package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.OrderDetail;
import com.example.restaurantmanagement.model.OrderDetail.OrderDetailId;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {

    // ✅ CREATE
    OrderDetail createOrderDetail(OrderDetail orderDetail);
    //UPDATE
    OrderDetail updateOrderDetail(OrderDetail orderDetail);

    // ✅ READ ONE (by composite key)
    Optional<OrderDetail> getOrderDetailById(OrderDetailId id);

    // ✅ READ ALL
    List<OrderDetail> getAllOrderDetails();

    // ✅ READ ALL BY ORDER ID
    List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);

    // ✅ DELETE
    void deleteOrderDetail(OrderDetailId id);
}
