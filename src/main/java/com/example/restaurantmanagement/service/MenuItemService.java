package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.MenuItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MenuItemService {

    // Tạo mới có ảnh
    MenuItem createMenuItem(MenuItem item, MultipartFile imageFile);

    // Cập nhật có ảnh
    MenuItem updateMenuItem(MenuItem item, MultipartFile imageFile);

    // Lấy tất cả
    List<MenuItem> getAllMenuItems();

    // Lấy theo ID
    Optional<MenuItem> getMenuItemById(Integer id);

    //  Xóa theo ID
    void deleteMenuItem(Integer id);

    // Tìm kiếm + sắp xếp theo giá
    List<MenuItem> searchAndSortMenuItems(String keyword);

    // Lọc theo phân loại món ăn
    List<MenuItem> getMenuItemsByCategory(String category);

    // Kiểm tra tên có trùng không
    boolean isNameUnique(String name);

    // Trả về phản hồi cho client về tên món
    Map<String, Object> checkNameAvailability(String name);
}
