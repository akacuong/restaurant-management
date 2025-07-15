package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createMenuItem(
            @RequestParam String name,
            @RequestParam BigDecimal price,
            @RequestParam String description,
            @RequestParam Integer categoryId,
            @RequestParam(required = false) MultipartFile imageFile
    ) {
        try {
            MenuItem item = new MenuItem();
            item.setName(name);
            item.setPrice(price);
            item.setDescription(description);

            MenuItem created = menuItemService.createMenuItem(item, imageFile, categoryId);
            return ResponseEntity.ok(new ResponseObject(created));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("CREATE_FAILED", ex.getMessage()));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllMenuItems() {
        List<MenuItem> items = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(new ResponseObject(items));
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getMenuItemById(@PathVariable Integer id) {
        Optional<MenuItem> optional = menuItemService.getMenuItemById(id);
        return optional.map(item -> ResponseEntity.ok(new ResponseObject(item)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Menu item not found")));
    }

    // UPDATE
    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateMenuItem(
            @RequestParam Integer id,
            @RequestParam String name,
            @RequestParam BigDecimal price,
            @RequestParam String description,
            @RequestParam Integer categoryId,
            @RequestParam(required = false) MultipartFile imageFile
    ) {
        try {
            MenuItem item = new MenuItem();
            item.setId(id);
            item.setName(name);
            item.setPrice(price);
            item.setDescription(description);

            MenuItem result = menuItemService.updateMenuItem(item, imageFile, categoryId);
            return ResponseEntity.ok(new ResponseObject(result));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("UPDATE_FAILED", ex.getMessage()));
        }
    }

    // DELETE
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteMenuItem(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");

        Optional<MenuItem> optional = menuItemService.getMenuItemById(id);
        if (optional.isPresent()) {
            menuItemService.deleteMenuItem(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Menu item deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", "Menu item not found"));
        }
    }

    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchMenuItems(@RequestParam String keyword) {
        return ResponseEntity.ok(new ResponseObject(menuItemService.searchAndSortMenuItems(keyword)));
    }

    @GetMapping("/category")
    public ResponseEntity<ResponseObject> getByCategory(@RequestParam Integer categoryId) {
        return ResponseEntity.ok(new ResponseObject(menuItemService.getMenuItemsByCategory(categoryId)));
    }

    // CHECK NAME UNIQUE
    @GetMapping("/check-name")
    public ResponseEntity<ResponseObject> checkName(@RequestParam String name) {
        Map<String, Object> result = menuItemService.checkNameAvailability(name);
        return ResponseEntity.ok(new ResponseObject(result));
    }
}
