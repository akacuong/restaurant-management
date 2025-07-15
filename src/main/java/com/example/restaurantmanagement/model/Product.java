package com.example.restaurantmanagement.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private BigDecimal unitPrice;

    private String unit;

    private Integer quantityInStock;

    private String description;
    @Column(nullable = false)
    private Integer minStockLevel;
    @Column(nullable = false)
    private Integer importanceScore = 5;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    // Constructors
    public Product() {}

    // Getters & Setters
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

    public Integer getQuantityInStock() {
        return quantityInStock;
    }
    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
    public Integer getImportanceScore() {
        return importanceScore;
    }

    public void setImportanceScore(Integer importanceScore) {
        this.importanceScore = importanceScore;
    }

}
