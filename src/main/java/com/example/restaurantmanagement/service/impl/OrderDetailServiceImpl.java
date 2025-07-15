package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.dto.MenuItemStats;
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

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

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

        int quantityToAdd = Math.max(orderDetail.getQuantity(), 0);

        // Check if detail already exists
        OrderDetailId id = new OrderDetailId(orderId, itemId);
        Optional<OrderDetail> existingOpt = orderDetailRepository.findById(id);

        OrderDetail result;
        if (existingOpt.isPresent()) {
            OrderDetail existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + quantityToAdd);
            result = orderDetailRepository.save(existing);
        } else {
            orderDetail.setOrder(order);
            orderDetail.setItem(item);
            orderDetail.setQuantity(quantityToAdd);
            result = orderDetailRepository.save(orderDetail);
        }

        recalculateOrderTotal(order);
        return result;
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

        int newQuantity = Math.max(updated.getQuantity(), 0);
        existing.setQuantity(newQuantity);
        orderDetailRepository.save(existing);

        recalculateOrderTotal(existing.getOrder());
        return existing;
    }

    @Override
    public void deleteOrderDetail(OrderDetailId id) {
        OrderDetail existing = orderDetailRepository.findById(id)
                .orElseThrow(() -> new NVException(ErrorCode.ORDER_DETAIL_NOT_FOUND, new Object[]{id}));

        orderDetailRepository.deleteById(id);
        recalculateOrderTotal(existing.getOrder());
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

    //  Hàm tính lại tổng tiền đơn hàng
    private void recalculateOrderTotal(Order order) {
        List<OrderDetail> details = orderDetailRepository.findByOrderOrderId(order.getId());
        BigDecimal total = BigDecimal.ZERO;

        for (OrderDetail detail : details) {
            BigDecimal lineTotal = detail.getItem().getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
            total = total.add(lineTotal);
        }

        order.setTotal(total);
        orderRepository.save(order);
    }
    @Override
    public Map<String, List<MenuItemStats>> getTopMenuItemsPerDayOfWeek(int topN) {
        List<OrderDetail> allDetails = orderDetailRepository.findAll();

        Map<DayOfWeek, List<OrderDetail>> groupedByDay = allDetails.stream()
                .filter(detail -> detail.getOrder() != null && detail.getOrder().getOrderTime() != null)
                .collect(Collectors.groupingBy(
                        detail -> detail.getOrder().getOrderTime().getDayOfWeek()
                ));

        Map<String, List<MenuItemStats>> result = new LinkedHashMap<>();

        for (Map.Entry<DayOfWeek, List<OrderDetail>> entry : groupedByDay.entrySet()) {
            Map<String, Integer> itemCount = entry.getValue().stream()
                    .collect(Collectors.groupingBy(
                            detail -> detail.getItem().getName(),
                            Collectors.summingInt(OrderDetail::getQuantity)
                    ));

            List<MenuItemStats> topItems = itemCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(topN)
                    .map(e -> new MenuItemStats(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            String dayName = entry.getKey().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            result.put(dayName, topItems);
        }

        return result;
    }


}
