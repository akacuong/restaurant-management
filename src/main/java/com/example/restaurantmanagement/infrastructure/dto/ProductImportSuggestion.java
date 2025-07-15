package com.example.restaurantmanagement.infrastructure.dto;

import java.math.BigDecimal;

public class ProductImportSuggestion {
    private Integer id;
    private String name;
    private BigDecimal unitPrice;
    private String unit;
    private Integer quantity; // Số lượng gợi ý nhập
    private Integer importanceScore;

    // Constructor
    public ProductImportSuggestion(Integer id, String name, BigDecimal unitPrice, String unit, Integer quantity, Integer importanceScore) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.quantity = quantity;
        this.importanceScore = importanceScore;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getImportanceScore() {
        return importanceScore;
    }

    public void setImportanceScore(Integer importanceScore) {
        this.importanceScore = importanceScore;
    }
}
