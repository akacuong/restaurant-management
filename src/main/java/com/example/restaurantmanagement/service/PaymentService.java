package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Payment;

import java.util.List;

public interface PaymentService {

    Payment savePayment(Payment payment);

    Payment getPaymentByOrderId(Integer orderId);

    List<Payment> getAllPayments();

    void deletePayment(Integer paymentId);
}
