package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.MenuItem;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MenuItemService {
    MenuItem createMenuItem(MenuItem item);
    MenuItem updateMenuItem(MenuItem item);
    List<MenuItem> getAllMenuItems();
    Optional<MenuItem> getMenuItemById(Integer id);
    void deleteMenuItem(Integer id);
    //  Tìm kiếm theo từ khóa (name/description), có sắp xếp theo giá
    List<MenuItem> searchAndSortMenuItems(String keyword);
    // Lấy các món ăn theo category (phân loại)
    List<MenuItem> getMenuItemsByCategory(String category);
    // Kiểm tra tên món ăn có trùng không (dùng Set)
    boolean isNameUnique(String name);
    Map<String, Object> checkNameAvailability(String name);
}
