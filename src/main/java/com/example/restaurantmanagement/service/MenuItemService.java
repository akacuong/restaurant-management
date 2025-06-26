package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    MenuItem createMenuItem(MenuItem item);               // ✅ CREATE
    MenuItem updateMenuItem(MenuItem item);               // ✅ UPDATE
    List<MenuItem> getAllMenuItems();                     // ✅ READ ALL
    Optional<MenuItem> getMenuItemById(Integer id);       // ✅ READ ONE
    void deleteMenuItem(Integer id);                      // ✅ DELETE
}
