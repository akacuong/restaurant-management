package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Payment;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    // READ BY ORDER ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseObject> getPaymentByOrderId(@PathVariable Integer orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId)
                .orElseThrow(() -> new NVException(ErrorCode.PAYMENT_NOT_FOUND, new Object[]{"Order ID: " + orderId}));
        return ResponseEntity.ok(new ResponseObject(payment));
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updatePayment(@RequestBody Payment updatedPayment) {
        if (updatedPayment == null || updatedPayment.getId() == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing payment ID or data"});
        }
        Payment updated = paymentService.updatePayment(updatedPayment);
        return ResponseEntity.ok(new ResponseObject(updated));
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(new ResponseObject(payments));
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createPayment(@RequestBody Payment payment) {
        if (payment == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Payment data is missing"});
        }
        Payment saved = paymentService.createPayment(payment);
        return ResponseEntity.ok(new ResponseObject(saved));
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deletePayment(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        if (id == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing payment ID"});
        }
        paymentService.deletePayment(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Payment deleted successfully"));
    }
}
