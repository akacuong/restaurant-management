package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.model.Order;
import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.model.TableInfo;
import com.example.restaurantmanagement.repository.CustomerRepository;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.repository.StaffRepository;
import com.example.restaurantmanagement.repository.TableInfoRepository;
import com.example.restaurantmanagement.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TableInfoRepository tableInfoRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            TableInfoRepository tableInfoRepository,
                            CustomerRepository customerRepository,
                            StaffRepository staffRepository) {
        this.orderRepository = orderRepository;
        this.tableInfoRepository = tableInfoRepository;
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public Order createOrder(Order order) {
        if (order.getId() != null) {
            throw new NVException(ErrorCode.ORDER_ID_MUST_BE_NULL);
        }

        if (order.getTable() == null || order.getTable().getId() == null) {
            throw new NVException(ErrorCode.TABLE_REQUIRED);
        }
        TableInfo table = tableInfoRepository.findById(order.getTable().getId())
                .orElseThrow(() -> new NVException(ErrorCode.TABLE_NOT_FOUND));
        order.setTable(table);

        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            throw new NVException(ErrorCode.CUSTOMER_REQUIRED);
        }
        Customer customer = customerRepository.findById(order.getCustomer().getId())
                .orElseThrow(() -> new NVException(ErrorCode.CUSTOMER_NOT_FOUND));
        order.setCustomer(customer);

        if (order.getStaff() == null || order.getStaff().getId() == null) {
            throw new NVException(ErrorCode.STAFF_REQUIRED);
        }
        Staff staff = staffRepository.findById(order.getStaff().getId())
                .orElseThrow(() -> new NVException(ErrorCode.STAFF_NOT_FOUND));
        order.setStaff(staff);

        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order updatedOrder) {
        if (updatedOrder.getId() == null) {
            throw new NVException(ErrorCode.ORDER_ID_REQUIRED);
        }

        Order order = orderRepository.findById(updatedOrder.getId())
                .orElseThrow(() -> new NVException(ErrorCode.ORDER_NOT_FOUND, new Object[]{updatedOrder.getId()}));

        order.setOrderTime(updatedOrder.getOrderTime());
        order.setStatus(updatedOrder.getStatus());
        order.setTotal(updatedOrder.getTotal());

        if (updatedOrder.getTable() != null && updatedOrder.getTable().getId() != null) {
            TableInfo table = tableInfoRepository.findById(updatedOrder.getTable().getId())
                    .orElseThrow(() -> new NVException(ErrorCode.TABLE_NOT_FOUND));
            order.setTable(table);
        }

        if (updatedOrder.getCustomer() != null && updatedOrder.getCustomer().getId() != null) {
            Customer customer = customerRepository.findById(updatedOrder.getCustomer().getId())
                    .orElseThrow(() -> new NVException(ErrorCode.CUSTOMER_NOT_FOUND));
            order.setCustomer(customer);
        }

        if (updatedOrder.getStaff() != null && updatedOrder.getStaff().getId() != null) {
            Staff staff = staffRepository.findById(updatedOrder.getStaff().getId())
                    .orElseThrow(() -> new NVException(ErrorCode.STAFF_NOT_FOUND));
            order.setStaff(staff);
        }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new NVException(ErrorCode.ORDER_NOT_FOUND, new Object[]{id});
        }
        orderRepository.deleteById(id);
    }
}
