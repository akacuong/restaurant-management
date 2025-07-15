package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
