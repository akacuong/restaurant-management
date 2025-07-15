package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Category;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createCategory(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        if (name == null || name.trim().isEmpty()) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing category name"});
        }
        Category category = categoryService.createCategory(name);
        return ResponseEntity.ok(new ResponseObject(category));
    }
    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateCategory(@RequestBody Map<String, Object> body) {
        if (!body.containsKey("id") || !body.containsKey("name")) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing 'id' or 'name'"});
        }
        Integer id = (Integer) body.get("id");
        String name = (String) body.get("name");

        if (id == null || name == null || name.trim().isEmpty()) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Invalid ID or name"});
        }

        Category updated = categoryService.updateCategory(id, name);
        return ResponseEntity.ok(new ResponseObject(updated));
    }
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteCategory(@RequestBody Map<String, Integer> body) {
        Integer id = body.get("id");
        if (id == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing category ID"});
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Category deleted successfully"));
    }
    @GetMapping
    public ResponseEntity<ResponseObject> getAllCategories() {
        List<Category> list = categoryService.getAllCategories();
        return ResponseEntity.ok(new ResponseObject(list));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new ResponseObject(category));
    }
}
