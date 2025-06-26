package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.Order;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ✅ CREATE
    @Override
    public Order createOrder(Order order) {
        if (order.getId() != null) {
            throw new IllegalArgumentException("New order must not have an ID");
        }
        return orderRepository.save(order);
    }

    // ✅ UPDATE
    @Override
    public Order updateOrder(Order updatedOrder) {
        if (updatedOrder.getId() == null) {
            throw new IllegalArgumentException("Order ID must not be null");
        }

        Optional<Order> existingOpt = orderRepository.findById(updatedOrder.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Order not found with ID = " + updatedOrder.getId());
        }

        Order existing = existingOpt.get();
        existing.setCustomer(updatedOrder.getCustomer());
        existing.setOrderTime(updatedOrder.getOrderTime());
        existing.setStatus(updatedOrder.getStatus());
        existing.setTotal(updatedOrder.getTotal());
        // 👉 Nếu có danh sách OrderDetails thì cần merge chúng ở đây nếu có

        return orderRepository.save(existing);
    }

    // ✅ READ ALL
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ READ BY ID
    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    // ✅ DELETE
    @Override
    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with ID = " + id);
        }
        orderRepository.deleteById(id);
    }
}
