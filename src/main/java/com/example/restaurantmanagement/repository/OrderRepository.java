package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository< Order,Integer> {
    List<Order> findByCustomer(Customer customer);
}
