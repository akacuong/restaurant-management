package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.ProductTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductTransactionService {

    ProductTransaction recordTransaction(Map<String, Object> body);
    List<ProductTransaction> getAllTransactions();

    List<ProductTransaction> getTransactionsByProduct(Integer productId);

    int getTotalImportedQuantity(Integer productId);

    int getTotalExportedQuantity(Integer productId);

    Map<String, Object> getDetailedReport(Integer productId);
    Map<String, Object> getImportSuggestions();

}
