package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId")
    Payment getPaymentByOrderId(@Param("orderId") Integer orderId);
}
