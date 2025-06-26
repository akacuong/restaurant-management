package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Payment;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ✅ CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createPayment(@RequestBody Payment payment) {
        try {
            Payment saved = paymentService.createPayment(payment);
            return ResponseEntity.ok(new ResponseObject(saved));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("CREATE_FAILED", ex.getMessage()));
        }
    }

    // ✅ READ BY ORDER ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseObject> getPaymentByOrderId(@PathVariable Integer orderId) {
        Optional<Payment> payment = paymentService.getPaymentByOrderId(orderId);
        return payment.map(value -> ResponseEntity.ok(new ResponseObject(value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Payment not found for Order ID: " + orderId)));
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(new ResponseObject(payments));
    }

    // ✅ DELETE
    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deletePayment(@PathVariable Integer id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Payment deleted successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", ex.getMessage()));
        }
    }
}
