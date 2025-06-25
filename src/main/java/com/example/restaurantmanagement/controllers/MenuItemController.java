package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;
    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem item) {
        return ResponseEntity.ok(menuItemService.saveMenuItem(item));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMenuItemById(@PathVariable Integer id) {
        Optional<MenuItem> optional = menuItemService.getMenuItemById(id);
        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //  UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Integer id, @RequestBody MenuItem updated) {
        Optional<MenuItem> optional = menuItemService.getMenuItemById(id);
        if (optional.isPresent()) {
            MenuItem existing = optional.get();
            existing.setName(updated.getName());
            existing.setPrice(updated.getPrice());
            existing.setDescription(updated.getDescription());
            existing.setCategory(updated.getCategory());
            return ResponseEntity.ok(menuItemService.saveMenuItem(existing));
        }
        return ResponseEntity.status(404).body("Menu item not found");
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Integer id) {
        if (menuItemService.getMenuItemById(id).isPresent()) {
            menuItemService.deleteMenuItem(id);
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.status(404).body("Menu item not found");
    }
}
