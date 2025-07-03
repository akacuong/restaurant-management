package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.service.FileStorageService;
import com.example.restaurantmanagement.service.MenuItemService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final FileStorageService fileStorageService;
    private final Set<String> uniqueNames = new HashSet<>();

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, FileStorageService fileStorageService) {
        this.menuItemRepository = menuItemRepository;
        this.fileStorageService = fileStorageService;

        List<MenuItem> existingItems = menuItemRepository.findAll();
        for (MenuItem item : existingItems) {
            uniqueNames.add(item.getName().toLowerCase());
        }
    }

    @Override
    public MenuItem createMenuItem(MenuItem item, MultipartFile imageFile) {
        if (item.getId() != null) {
            throw new NVException(ErrorCode.MENU_ITEM_ID_MUST_BE_NULL);
        }

        if (!isNameUnique(item.getName())) {
            throw new NVException(ErrorCode.MENU_ITEM_NAME_DUPLICATE, new Object[]{item.getName()});
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String savedPath = fileStorageService.saveFile(imageFile, "menu_items");
            item.setImage(savedPath);
        }

        uniqueNames.add(item.getName().toLowerCase());
        return menuItemRepository.save(item);
    }

    @Override
    public MenuItem updateMenuItem(MenuItem updatedItem, MultipartFile imageFile) {
        if (updatedItem.getId() == null) {
            throw new NVException(ErrorCode.MENU_ITEM_ID_REQUIRED);
        }

        Optional<MenuItem> existingOpt = menuItemRepository.findById(updatedItem.getId());
        if (existingOpt.isEmpty()) {
            throw new NVException(ErrorCode.MENU_ITEM_NOT_FOUND, new Object[]{updatedItem.getId()});
        }

        MenuItem existing = existingOpt.get();

        if (!existing.getName().equalsIgnoreCase(updatedItem.getName())) {
            uniqueNames.remove(existing.getName().toLowerCase());
            if (!isNameUnique(updatedItem.getName())) {
                throw new NVException(ErrorCode.MENU_ITEM_NAME_DUPLICATE, new Object[]{updatedItem.getName()});
            }
            uniqueNames.add(updatedItem.getName().toLowerCase());
        }

        existing.setName(updatedItem.getName());
        existing.setPrice(updatedItem.getPrice());
        existing.setCategory(updatedItem.getCategory());
        existing.setDescription(updatedItem.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            String savedPath = fileStorageService.saveFile(imageFile, "menu_items");
            existing.setImage(savedPath);
        }

        return menuItemRepository.save(existing);
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public Optional<MenuItem> getMenuItemById(Integer id) {
        return menuItemRepository.findById(id);
    }

    @Override
    public void deleteMenuItem(Integer id) {
        Optional<MenuItem> existingOpt = menuItemRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new NVException(ErrorCode.MENU_ITEM_NOT_FOUND, new Object[]{id});
        }

        uniqueNames.remove(existingOpt.get().getName().toLowerCase());
        menuItemRepository.deleteById(id);
    }

    @Override
    public List<MenuItem> searchAndSortMenuItems(String keyword) {
        return menuItemRepository.findAll().stream()
                .filter(item -> item.getName().toLowerCase().contains(keyword.toLowerCase())
                        || item.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .sorted(Comparator.comparing(MenuItem::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.findAll().stream()
                .filter(item -> item.getCategory() != null && item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isNameUnique(String name) {
        return !uniqueNames.contains(name.toLowerCase());
    }

    @Override
    public Map<String, Object> checkNameAvailability(String name) {
        boolean unique = isNameUnique(name);
        Map<String, Object> response = new HashMap<>();
        response.put("name", name);
        response.put("isUnique", unique);
        return response;
    }
}