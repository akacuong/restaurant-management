package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.dto.ProductImportSuggestion;
import com.example.restaurantmanagement.model.Category;
import com.example.restaurantmanagement.model.Product;
import com.example.restaurantmanagement.model.ProductTransaction;
import com.example.restaurantmanagement.model.ProductTransaction.Type;
import com.example.restaurantmanagement.repository.CategoryRepository;
import com.example.restaurantmanagement.repository.ProductRepository;
import com.example.restaurantmanagement.repository.ProductTransactionRepository;
import com.example.restaurantmanagement.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductTransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;


    public ProductServiceImpl(ProductRepository productRepository,
                              ProductTransactionRepository transactionRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(Map<String, String> body) {
        String name = body.get("name");
        String description = body.get("description");
        String unit = body.getOrDefault("unit", "");
        BigDecimal unitPrice = body.get("unitPrice") != null ? new BigDecimal(body.get("unitPrice")) : BigDecimal.ZERO;
        Integer quantityInStock = body.get("quantityInStock") != null ? Integer.parseInt(body.get("quantityInStock")) : 0;
        Integer categoryId = body.get("categoryId") != null ? Integer.parseInt(body.get("categoryId")) : null;
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setUnit(unit);
        product.setUnitPrice(unitPrice);
        product.setQuantityInStock(quantityInStock);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Map<String, Object> body) {
        Integer id = (Integer) body.get("id");
        if (id == null) {
            throw new RuntimeException("Missing product ID");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        String name = (String) body.get("name");
        String description = (String) body.getOrDefault("description", "");
        String unit = (String) body.getOrDefault("unit", "");
        BigDecimal unitPrice = body.get("unitPrice") != null ? new BigDecimal(body.get("unitPrice").toString()) : BigDecimal.ZERO;
        Integer quantityInStock = (body.get("quantityInStock") instanceof Integer)
                ? (Integer) body.get("quantityInStock") : 0;
        Integer categoryId = (body.get("categoryId") instanceof Integer) ? (Integer) body.get("categoryId") : null;
        product.setName(name);
        product.setDescription(description);
        product.setUnit(unit);
        product.setUnitPrice(unitPrice);
        product.setQuantityInStock(quantityInStock);

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public ProductTransaction recordTransaction(Map<String, Object> body) {
        Integer productId = (Integer) body.get("productId");
        Integer quantity = (Integer) body.get("quantity");
        String typeStr = (String) body.get("type");
        BigDecimal unitPrice = new BigDecimal(body.get("unitPrice").toString());

        if (productId == null || quantity == null || typeStr == null || unitPrice == null) {
            throw new RuntimeException("Missing transaction info");
        }

        ProductTransaction.Type type = ProductTransaction.Type.valueOf(typeStr.toUpperCase());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        ProductTransaction transaction = new ProductTransaction();
        transaction.setProduct(product);
        transaction.setQuantity(quantity);
        transaction.setUnitPrice(unitPrice);
        transaction.setType(type);
        transaction.setTransactionTime(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Override
    public List<ProductTransaction> getTransactionsByProduct(Integer productId) {
        return transactionRepository.findByProductId(productId);
    }

    @Override
    public Map<String, Object> getInventoryReport(Integer productId) {
        List<ProductTransaction> transactions = transactionRepository.findByProductId(productId);

        int totalImport = transactions.stream()
                .filter(tx -> tx.getType() == Type.IMPORT)
                .mapToInt(ProductTransaction::getQuantity)
                .sum();

        int totalExport = transactions.stream()
                .filter(tx -> tx.getType() == Type.EXPORT)
                .mapToInt(ProductTransaction::getQuantity)
                .sum();
        int currentStock = totalImport - totalExport;
        Map<String, Object> report = new HashMap<>();
        report.put("productId", productId);
        report.put("stock", currentStock);
        report.put("totalImport", totalImport);
        report.put("totalExport", totalExport);

        return report;
    }

    @Override
    public Map<String, Object> getProductUsageReport(Integer productId) {
        List<ProductTransaction> transactions = transactionRepository.findByProductId(productId);

        int totalUsed = transactions.stream()
                .filter(tx -> tx.getType() == Type.EXPORT)
                .mapToInt(ProductTransaction::getQuantity)
                .sum();

        BigDecimal totalCost = transactions.stream()
                .filter(tx -> tx.getType() == Type.IMPORT)
                .map(tx -> tx.getUnitPrice().multiply(BigDecimal.valueOf(tx.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> report = new HashMap<>();
        report.put("productId", productId);
        report.put("totalUsed", totalUsed);
        report.put("totalPurchaseCost", totalCost);
        return report;
    }

    @Override
    public List<Product> getLowStockWarnings() {
        return productRepository.findLowStockProducts();
    }
    @Override
    public List<ProductImportSuggestion> optimizeImportWithBudget(BigDecimal budget) {
        List<Product> products = productRepository.findAll();
        int n = products.size();
        int maxBudget = budget.intValue();

        int[] price = new int[n];
        double[] score = new double[n];
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // Tính điểm cho từng sản phẩm
        for (int i = 0; i < n; i++) {
            Product p = products.get(i);
            price[i] = p.getUnitPrice().intValue();
            int importance = p.getImportanceScore() != null ? p.getImportanceScore() : 1;
            int stock = p.getQuantityInStock() != null ? p.getQuantityInStock() : 0;
            int shortage = Math.max(1, 100 - stock);
            int usageInWeek = transactionRepository.findByProductId(p.getId()).stream()
                    .filter(tx -> tx.getType() == Type.EXPORT)
                    .filter(tx -> tx.getTransactionTime().isAfter(sevenDaysAgo))
                    .mapToInt(ProductTransaction::getQuantity)
                    .sum();

            score[i] = ((0.4 * shortage) + (0.3 * importance) + (0.3 * usageInWeek)) / (price[i] == 0 ? 1 : price[i]);
        }
        // B1: Sắp xếp theo hiệu suất score/price giảm dần
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i);
        indices.sort((a, b) -> Double.compare(score[b], score[a]));
        // B2: Chọn ít nhất 5 sản phẩm khác nhau đầu tiên
        Map<Integer, Integer> countMap = new LinkedHashMap<>();
        int remaining = maxBudget;

        for (int i = 0; i < n && countMap.size() < 5; i++) {
            int idx = indices.get(i);
            if (price[idx] <= remaining) {
                countMap.put(idx, 1);
                remaining -= price[idx];
            }
        }
        // B3: Sau đó tiếp tục dùng unbounded knapsack với phần ngân sách còn lại
        while (remaining > 0) {
            boolean added = false;
            for (int i = 0; i < n; i++) {
                int idx = indices.get(i);
                if (price[idx] <= remaining) {
                    countMap.put(idx, countMap.getOrDefault(idx, 0) + 1);
                    remaining -= price[idx];
                    added = true;
                    break;
                }
            }
            if (!added) break; // Không thể thêm gì nữa
        }

        // B4: Tạo kết quả
        List<ProductImportSuggestion> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            Product p = products.get(entry.getKey());
            result.add(new ProductImportSuggestion(
                    p.getId(),
                    p.getName(),
                    p.getUnitPrice(),
                    p.getUnit(),
                    entry.getValue(),
                    p.getImportanceScore()
            ));
        }

        return result;
    }
}
