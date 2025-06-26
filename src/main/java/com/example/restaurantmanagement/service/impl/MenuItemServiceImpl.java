package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    // ✅ CREATE
    @Override
    public MenuItem createMenuItem(MenuItem item) {
        if (item.getId() != null) {
            throw new IllegalArgumentException("New menu item must not have an ID");
        }
        return menuItemRepository.save(item);
    }

    // ✅ UPDATE
    @Override
    public MenuItem updateMenuItem(MenuItem updatedItem) {
        if (updatedItem.getId() == null) {
            throw new IllegalArgumentException("Menu item ID must not be null");
        }

        Optional<MenuItem> existingOpt = menuItemRepository.findById(updatedItem.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Menu item not found with ID = " + updatedItem.getId());
        }

        MenuItem existing = existingOpt.get();
        existing.setName(updatedItem.getName());
        existing.setPrice(updatedItem.getPrice());
        existing.setCategory(updatedItem.getCategory());
        existing.setDescription(updatedItem.getDescription());

        return menuItemRepository.save(existing);
    }

    // ✅ READ ALL
    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    // ✅ READ BY ID
    @Override
    public Optional<MenuItem> getMenuItemById(Integer id) {
        return menuItemRepository.findById(id);
    }

    // ✅ DELETE
    @Override
    public void deleteMenuItem(Integer id) {
        if (!menuItemRepository.existsById(id)) {
            throw new RuntimeException("Menu item not found with ID = " + id);
        }
        menuItemRepository.deleteById(id);
    }
}
