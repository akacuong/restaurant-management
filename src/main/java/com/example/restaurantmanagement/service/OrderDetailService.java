package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.OrderDetail;
import com.example.restaurantmanagement.model.OrderDetail.OrderDetailId;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {

    OrderDetail saveOrderDetail(OrderDetail orderDetail);  // ✅ Tên đúng phải là saveOrderDetail

    Optional<OrderDetail> getOrderDetailById(OrderDetailId id);

    List<OrderDetail> getAllOrderDetails();

    List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);

    void deleteOrderDetail(OrderDetailId id);
}
