package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.Order;
import com.example.restaurantmanagement.model.Payment;
import com.example.restaurantmanagement.repository.PaymentRepository;
import com.example.restaurantmanagement.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ✅ CREATE
    @Override
    public Payment createPayment(Payment payment) {
        if (payment.getId() != null) {
            throw new IllegalArgumentException("New payment must not have an ID");
        }

        if (payment.getPaymentTime() == null) {
            payment.setPaymentTime(LocalDateTime.now());
        }

        Order order = payment.getOrder();
        if (order != null) {
            order.setStatus("Thanh toán thành công");
        }

        return paymentRepository.save(payment);
    }

    // ✅ UPDATE
    @Override
    public Payment updatePayment(Payment updated) {
        if (updated.getId() == null) {
            throw new IllegalArgumentException("Payment ID must not be null for update");
        }

        Optional<Payment> existingOpt = paymentRepository.findById(updated.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Payment not found with ID = " + updated.getId());
        }

        Payment existing = existingOpt.get();
        existing.setAmount(updated.getAmount());
        existing.setMethod(updated.getMethod());
        existing.setPaymentTime(updated.getPaymentTime());
        existing.setOrder(updated.getOrder());

        return paymentRepository.save(existing);
    }

    // ✅ READ BY ID
    @Override
    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    // ✅ READ BY ORDER ID
    @Override
    public Optional<Payment> getPaymentByOrderId(Integer orderId) {
        return Optional.ofNullable(paymentRepository.getPaymentByOrderId(orderId));
    }

    // ✅ READ ALL
    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // ✅ DELETE
    @Override
    public void deletePayment(Integer id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with ID = " + id);
        }
        paymentRepository.deleteById(id);
    }
}
