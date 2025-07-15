package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.quantityInStock < p.minStockLevel")
    List<Product> findLowStockProducts();
}