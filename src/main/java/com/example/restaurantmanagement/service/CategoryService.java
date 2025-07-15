package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Category;

import java.util.List;

public interface CategoryService {

    // Tạo mới category theo tên
    Category createCategory(String name);

    // Lấy tất cả danh mục
    List<Category> getAllCategories();

    Category getCategoryById(Integer id);

    // Cập nhật tên danh mục theo ID
    Category updateCategory(Integer id, String name);

    // Xóa danh mục theo ID
    void deleteCategory(Integer id);
}
