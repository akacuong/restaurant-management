package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    MenuItem saveMenuItem(MenuItem item);
    List<MenuItem> getAllMenuItems();
    Optional<MenuItem> getMenuItemById(Integer id);
    void deleteMenuItem(Integer id);
}
