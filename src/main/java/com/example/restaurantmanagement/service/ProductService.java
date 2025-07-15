package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.infrastructure.dto.ProductImportSuggestion;
import com.example.restaurantmanagement.model.Product;
import com.example.restaurantmanagement.model.ProductTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Map<String, String> body);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Integer id);
    Product updateProduct(Map<String, Object> body);
    void deleteProduct(Integer id);
    ProductTransaction recordTransaction(Map<String, Object> body);
    List<ProductTransaction> getTransactionsByProduct(Integer productId);
    Map<String, Object> getInventoryReport(Integer productId);
    Map<String, Object> getProductUsageReport(Integer productId);
    //Cảnh báo nguyên liệu sắp hết
    List<Product> getLowStockWarnings();
    //Nhập nguyên liệu theo ngân sách(Knapsack)
    List<ProductImportSuggestion> optimizeImportWithBudget(BigDecimal budget);


}
