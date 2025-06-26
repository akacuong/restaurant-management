package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    // ✅ CREATE
    Payment createPayment(Payment payment);

    // ✅ UPDATE
    Payment updatePayment(Payment payment);

    // ✅ READ ONE BY ID
    Optional<Payment> getPaymentById(Integer id);

    // ✅ READ ONE BY Order ID
    Optional<Payment> getPaymentByOrderId(Integer orderId);

    // ✅ READ ALL
    List<Payment> getAllPayments();

    // ✅ DELETE
    void deletePayment(Integer id);
}
