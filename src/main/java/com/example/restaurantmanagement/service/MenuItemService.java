package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.MenuItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MenuItemService {

    // Tạo mới món ăn có ảnh và categoryId
    MenuItem createMenuItem(MenuItem item, MultipartFile imageFile, Integer categoryId);

    // Cập nhật món ăn có ảnh và categoryId
    MenuItem updateMenuItem(MenuItem item, MultipartFile imageFile, Integer categoryId);

    // Lấy tất cả menu item
    List<MenuItem> getAllMenuItems();

    // Lấy theo ID
    Optional<MenuItem> getMenuItemById(Integer id);

    // Xóa theo ID
    void deleteMenuItem(Integer id);

    // Tìm kiếm và sắp xếp
    List<MenuItem> searchAndSortMenuItems(String keyword);

    // Lấy menu theo category ID
    List<MenuItem> getMenuItemsByCategory(Integer categoryId);

    // Kiểm tra tên có trùng không
    boolean isNameUnique(String name);

    // Trả về phản hồi cho client về tên món
    Map<String, Object> checkNameAvailability(String name);
    //Search by keyword

}
