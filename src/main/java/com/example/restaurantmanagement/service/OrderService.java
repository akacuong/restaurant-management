package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order saveOrder(Order order);

    Optional<Order> getOrderById(Integer id);

    List<Order> getAllOrders();

    void deleteOrder(Integer id);
}
