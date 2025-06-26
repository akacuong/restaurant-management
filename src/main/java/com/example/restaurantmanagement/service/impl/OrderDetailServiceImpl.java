package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.model.Order;
import com.example.restaurantmanagement.model.OrderDetail;
import com.example.restaurantmanagement.model.OrderDetail.OrderDetailId;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.repository.OrderDetailRepository;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository,
                                  OrderRepository orderRepository,
                                  MenuItemRepository menuItemRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // ✅ CREATE
    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        if (orderDetail.getOrder() == null || orderDetail.getItem() == null) {
            throw new IllegalArgumentException("Order and MenuItem must not be null");
        }

        Integer orderId = orderDetail.getOrder().getId();
        Integer itemId = orderDetail.getItem().getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID = " + orderId));

        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("MenuItem not found with ID = " + itemId));

        // Gán lại thực thể đã tồn tại để tránh lỗi transient
        orderDetail.setOrder(order);
        orderDetail.setItem(item);

        return orderDetailRepository.save(orderDetail);
    }

    // ✅ UPDATE
    @Override
    public OrderDetail updateOrderDetail(OrderDetail updated) {
        if (updated.getOrder() == null || updated.getItem() == null) {
            throw new IllegalArgumentException("Order and MenuItem must not be null for update");
        }

        OrderDetailId id = new OrderDetailId(
                updated.getOrder().getId(),
                updated.getItem().getId()
        );

        OrderDetail existing = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found with ID: " + id));

        existing.setQuantity(updated.getQuantity());

        return orderDetailRepository.save(existing);
    }

    // ✅ READ ONE
    @Override
    public Optional<OrderDetail> getOrderDetailById(OrderDetailId id) {
        return orderDetailRepository.findById(id);
    }

    // ✅ READ ALL
    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    // ✅ READ BY ORDER ID
    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderOrderId(orderId);
    }

    // ✅ DELETE
    @Override
    public void deleteOrderDetail(OrderDetailId id) {
        if (!orderDetailRepository.existsById(id)) {
            throw new RuntimeException("OrderDetail not found with ID: " + id);
        }
        orderDetailRepository.deleteById(id);
    }
}
