package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment updatePayment(Payment payment);
    Optional<Payment> getPaymentById(Integer id);
    Optional<Payment> getPaymentByOrderId(Integer orderId);
    List<Payment> getAllPayments();
    void deletePayment(Integer id);
}
