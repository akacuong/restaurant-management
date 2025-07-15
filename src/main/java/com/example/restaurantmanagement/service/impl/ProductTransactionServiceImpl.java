package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Product;
import com.example.restaurantmanagement.model.ProductTransaction;
import com.example.restaurantmanagement.repository.ProductRepository;
import com.example.restaurantmanagement.repository.ProductTransactionRepository;
import com.example.restaurantmanagement.service.ProductTransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductTransactionServiceImpl implements ProductTransactionService {

    private final ProductTransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    public ProductTransactionServiceImpl(ProductTransactionRepository transactionRepository,
                                         ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
    }
    @Override
    public ProductTransaction recordTransaction(Map<String, Object> body) {
        Integer productId = (Integer) body.get("productId");
        Integer quantity = (Integer) body.get("quantity");
        String typeStr = (String) body.get("type");
        BigDecimal unitPrice = new BigDecimal(body.get("unitPrice").toString());

        if (productId == null || quantity == null || typeStr == null || unitPrice == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing or invalid transaction data"});
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NVException(ErrorCode.NOT_FOUND, new Object[]{"Product not found"}));
        ProductTransaction.Type type = ProductTransaction.Type.valueOf(typeStr.toUpperCase());
        ProductTransaction tx = new ProductTransaction();
        tx.setProduct(product);
        tx.setQuantity(quantity);
        tx.setUnitPrice(unitPrice);
        tx.setType(type);
        tx.setTransactionTime(LocalDateTime.now());

        return transactionRepository.save(tx);
    }
    @Override
    public List<ProductTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<ProductTransaction> getTransactionsByProduct(Integer productId) {
        return transactionRepository.findAll().stream()
                .filter(t -> t.getProduct() != null && t.getProduct().getId().equals(productId))
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalImportedQuantity(Integer productId) {
        return getTransactionsByProduct(productId).stream()
                .filter(tx -> tx.getType() == ProductTransaction.Type.IMPORT)
                .mapToInt(ProductTransaction::getQuantity)
                .sum();
    }

    @Override
    public int getTotalExportedQuantity(Integer productId) {
        return getTransactionsByProduct(productId).stream()
                .filter(tx -> tx.getType() == ProductTransaction.Type.EXPORT)
                .mapToInt(ProductTransaction::getQuantity)
                .sum();
    }
    @Override
    public Map<String, Object> getImportSuggestions() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        List<ProductTransaction> recentExports = transactionRepository.findByTypeAndTransactionTimeAfter(
                ProductTransaction.Type.EXPORT, oneWeekAgo
        );
        // Gom nhóm theo sản phẩm
        Map<Product, Integer> usedQuantities = recentExports.stream()
                .collect(Collectors.groupingBy(
                        ProductTransaction::getProduct,
                        Collectors.summingInt(ProductTransaction::getQuantity)
                ));
        List<Map<String, Object>> suggestions = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : usedQuantities.entrySet()) {
            Product product = entry.getKey();
            int quantityUsedLastWeek = entry.getValue();
            int currentStock = product.getQuantityInStock();

            int suggestedImport = quantityUsedLastWeek - currentStock;
            if (suggestedImport > 0) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("productId", product.getId());
                suggestion.put("productName", product.getName());
                suggestion.put("currentStock", currentStock);
                suggestion.put("usedLast7Days", quantityUsedLastWeek);
                suggestion.put("suggestedImport", suggestedImport);
                suggestions.add(suggestion);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", LocalDateTime.now());
        result.put("suggestions", suggestions);
        return result;
    }


    @Override
    public Map<String, Object> getDetailedReport(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        int imported = getTotalImportedQuantity(productId);
        int exported = getTotalExportedQuantity(productId);
        int stock = imported - exported;
        BigDecimal totalImportCost = getTransactionsByProduct(productId).stream()
                .filter(tx -> tx.getType() == ProductTransaction.Type.IMPORT)
                .map(tx -> tx.getUnitPrice().multiply(BigDecimal.valueOf(tx.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Object> report = new HashMap<>();
        report.put("productId", product.getId());
        report.put("productName", product.getName());
        report.put("stock", stock);
        report.put("importedQuantity", imported);
        report.put("exportedQuantity", exported);
        report.put("totalImportCost", totalImportCost);
        return report;
    }
}
