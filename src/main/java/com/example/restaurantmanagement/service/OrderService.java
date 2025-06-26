package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(Order order);                       // ✅ CREATE

    Order updateOrder(Order order);                       // ✅ UPDATE

    Optional<Order> getOrderById(Integer id);             // ✅ READ ONE

    List<Order> getAllOrders();                           // ✅ READ ALL

    void deleteOrder(Integer id);                         // ✅ DELETE
}
