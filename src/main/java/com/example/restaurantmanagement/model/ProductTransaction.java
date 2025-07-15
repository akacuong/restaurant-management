package com.example.restaurantmanagement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class ProductTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết đến sản phẩm
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private BigDecimal unitPrice;

    private LocalDateTime transactionTime;

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        IMPORT,
        EXPORT
    }
    public ProductTransaction() {
        this.transactionTime = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
