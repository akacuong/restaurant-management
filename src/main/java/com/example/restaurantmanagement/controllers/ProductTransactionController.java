package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.ProductTransaction;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.ProductTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product-transactions")
public class ProductTransactionController {

    private final ProductTransactionService transactionService;

    public ProductTransactionController(ProductTransactionService transactionService) {
        this.transactionService = transactionService;
    }
    // Ghi nhận giao dịch mới (nhập/xuất kho)
    @PostMapping("/record")
    public ResponseEntity<ResponseObject> recordTransaction(@RequestBody Map<String, Object> body) {
        ProductTransaction transaction = transactionService.recordTransaction(body);
        return ResponseEntity.ok(new ResponseObject(transaction));
    }
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<ResponseObject> getTransactionsByProduct(@PathVariable Integer productId) {
        List<ProductTransaction> list = transactionService.getTransactionsByProduct(productId);
        return ResponseEntity.ok(new ResponseObject(list));
    }
    @GetMapping
    public ResponseEntity<ResponseObject> getAllTransactions() {
        List<ProductTransaction> list = transactionService.getAllTransactions();
        return ResponseEntity.ok(new ResponseObject(list));
    }
    @GetMapping("/report/{productId}")
    public ResponseEntity<ResponseObject> getReport(@PathVariable Integer productId) {
        Map<String, Object> report = transactionService.getDetailedReport(productId);
        return ResponseEntity.ok(new ResponseObject(report));
    }
    @GetMapping("/import-suggestions")
    public ResponseEntity<ResponseObject> getImportSuggestions() {
        Map<String, Object> result = transactionService.getImportSuggestions();
        return ResponseEntity.ok(new ResponseObject(result));
    }
}
