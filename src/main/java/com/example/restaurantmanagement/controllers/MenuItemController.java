package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // ✅ CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createMenuItem(@RequestBody MenuItem item) {
        try {
            MenuItem created = menuItemService.createMenuItem(item);
            return ResponseEntity.ok(new ResponseObject(created));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("CREATE_FAILED", ex.getMessage()));
        }
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllMenuItems() {
        List<MenuItem> items = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(new ResponseObject(items));
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getMenuItemById(@PathVariable Integer id) {
        Optional<MenuItem> optional = menuItemService.getMenuItemById(id);
        return optional.map(item -> ResponseEntity.ok(new ResponseObject(item)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Menu item not found")));
    }

    // ✅ UPDATE
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateMenuItem(@PathVariable Integer id,
                                                         @RequestBody MenuItem updated) {
        try {
            updated.setId(id); // set ID từ path
            MenuItem result = menuItemService.updateMenuItem(updated);
            return ResponseEntity.ok(new ResponseObject(result));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("UPDATE_FAILED", ex.getMessage()));
        }
    }

    // ✅ DELETE
    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteMenuItem(@PathVariable Integer id) {
        Optional<MenuItem> optional = menuItemService.getMenuItemById(id);
        if (optional.isPresent()) {
            menuItemService.deleteMenuItem(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Menu item deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", "Menu item not found"));
        }
    }
}
