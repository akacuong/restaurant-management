package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.infrastructure.dto.ProductImportSuggestion;
import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Product;
import com.example.restaurantmanagement.model.ProductTransaction;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createProduct(@RequestBody Map<String, String> body) {
        Product product = productService.createProduct(body);
        return ResponseEntity.ok(new ResponseObject(product));
    }

    // UPDATE
    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateProduct(@RequestBody Map<String, Object> body) {
        Product updated = productService.updateProduct(body);
        return ResponseEntity.ok(new ResponseObject(updated));
    }

    // DELETE
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteProduct(@RequestBody Map<String, Integer> body) {
        Integer id = body.get("id");
        if (id == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing product ID"});
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Product deleted successfully"));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllProducts() {
        List<Product> list = productService.getAllProducts();
        return ResponseEntity.ok(new ResponseObject(list));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new NVException(ErrorCode.NOT_FOUND, new Object[]{"Product not found"}));
        return ResponseEntity.ok(new ResponseObject(product));
    }
    // RECORD TRANSACTION (IMPORT / EXPORT)
    @PostMapping("/transaction")
    public ResponseEntity<ResponseObject> recordTransaction(@RequestBody Map<String, Object> body) {
        ProductTransaction tx = productService.recordTransaction(body);
        return ResponseEntity.ok(new ResponseObject(tx));
    }
    // GET TRANSACTIONS BY PRODUCT
    @GetMapping("/{id}/transactions")
    public ResponseEntity<ResponseObject> getTransactions(@PathVariable Integer id) {
        return ResponseEntity.ok(new ResponseObject(productService.getTransactionsByProduct(id)));
    }

    // INVENTORY REPORT
    @GetMapping("/{id}/inventory-report")
    public ResponseEntity<ResponseObject> getInventoryReport(@PathVariable Integer id) {
        return ResponseEntity.ok(new ResponseObject(productService.getInventoryReport(id)));
    }

    // USAGE REPORT
    @GetMapping("/{id}/usage-report")
    public ResponseEntity<ResponseObject> getUsageReport(@PathVariable Integer id) {
        return ResponseEntity.ok(new ResponseObject(productService.getProductUsageReport(id)));
    }
    // LOW STOCK WARNINGS (Cảnh báo nguyên liệu sắp hết)
    @GetMapping("/low-stock")
    public ResponseEntity<ResponseObject> getLowStockWarnings() {
        List<Product> lowStockList = productService.getLowStockWarnings();
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Low stock product list", lowStockList));
    }
    @PostMapping("/optimize-import")
    public ResponseEntity<ResponseObject> optimizeImport(@RequestBody Map<String, Object> body) {
        BigDecimal budget = new BigDecimal(body.get("budget").toString());
        List<ProductImportSuggestion> result = productService.optimizeImportWithBudget(budget);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Optimized import plan", result));
    }
}
