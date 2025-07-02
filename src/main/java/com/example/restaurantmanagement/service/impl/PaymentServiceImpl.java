package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
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

    @Override
    public Payment createPayment(Payment payment) {
        if (payment.getId() != null) {
            throw new NVException(ErrorCode.PAYMENT_ID_MUST_BE_NULL_WHEN_CREATING);
        }

        if (payment.getPaymentTime() == null) {
            payment.setPaymentTime(LocalDateTime.now());
        }

        Order order = payment.getOrder();
        if (order != null) {
            order.setStatus("paid successfully");
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Payment updated) {
        if (updated.getId() == null) {
            throw new NVException(ErrorCode.PAYMENT_ID_REQUIRED_FOR_UPDATE);
        }

        Payment existing = paymentRepository.findById(updated.getId())
                .orElseThrow(() -> new NVException(ErrorCode.PAYMENT_NOT_FOUND, new Object[]{updated.getId()}));

        existing.setAmount(updated.getAmount());
        existing.setMethod(updated.getMethod());
        existing.setPaymentTime(updated.getPaymentTime());
        existing.setOrder(updated.getOrder());

        return paymentRepository.save(existing);
    }

    @Override
    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Optional<Payment> getPaymentByOrderId(Integer orderId) {
        return Optional.ofNullable(paymentRepository.getPaymentByOrderId(orderId));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public void deletePayment(Integer id) {
        if (!paymentRepository.existsById(id)) {
            throw new NVException(ErrorCode.PAYMENT_NOT_FOUND, new Object[]{id});
        }
        paymentRepository.deleteById(id);
    }
}
