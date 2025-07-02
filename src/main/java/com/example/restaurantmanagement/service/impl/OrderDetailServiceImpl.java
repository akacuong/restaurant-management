package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
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

    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        if (orderDetail.getOrder() == null || orderDetail.getItem() == null) {
            throw new NVException(ErrorCode.ORDER_OR_MENUITEM_REQUIRED);
        }

        Integer orderId = orderDetail.getOrder().getId();
        Integer itemId = orderDetail.getItem().getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NVException(ErrorCode.ORDER_NOT_FOUND, new Object[]{orderId}));

        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new NVException(ErrorCode.MENU_ITEM_NOT_FOUND, new Object[]{itemId}));

        orderDetail.setOrder(order);
        orderDetail.setItem(item);

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail updateOrderDetail(OrderDetail updated) {
        if (updated.getOrder() == null || updated.getItem() == null) {
            throw new NVException(ErrorCode.ORDER_OR_MENUITEM_REQUIRED);
        }

        OrderDetailId id = new OrderDetailId(
                updated.getOrder().getId(),
                updated.getItem().getId()
        );

        OrderDetail existing = orderDetailRepository.findById(id)
                .orElseThrow(() -> new NVException(ErrorCode.ORDER_DETAIL_NOT_FOUND, new Object[]{id}));

        existing.setQuantity(updated.getQuantity());

        return orderDetailRepository.save(existing);
    }

    @Override
    public Optional<OrderDetail> getOrderDetailById(OrderDetailId id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderOrderId(orderId);
    }

    @Override
    public void deleteOrderDetail(OrderDetailId id) {
        if (!orderDetailRepository.existsById(id)) {
            throw new NVException(ErrorCode.ORDER_DETAIL_NOT_FOUND, new Object[]{id});
        }
        orderDetailRepository.deleteById(id);
    }
}
