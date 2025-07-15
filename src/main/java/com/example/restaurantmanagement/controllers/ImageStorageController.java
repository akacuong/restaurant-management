package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.ImageStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageStorageController {

    private final ImageStorageService imageStorageService;

    public ImageStorageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }
    // Upload nhiều ảnh cho Customer
    @PostMapping("/upload/customer")
    public ResponseEntity<ResponseObject> uploadCustomerImages(
            @RequestParam("customerId") Integer customerId,
            @RequestParam("files") MultipartFile[] files) {
        try {
            imageStorageService.uploadCustomerImages(customerId, files);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Uploaded customer images successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("UPLOAD_FAILED", e.getMessage()));
        }
    }

    // Upload nhiều ảnh cho Staff
    @PostMapping("/upload/staff")
    public ResponseEntity<ResponseObject> uploadStaffImages(
            @RequestParam("staffId") Integer staffId,
            @RequestParam("files") MultipartFile[] files) {
        try {
            imageStorageService.uploadStaffImages(staffId, files);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Uploaded staff images successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("UPLOAD_FAILED", e.getMessage()));
        }
    }
    @PostMapping("/update/customer")
    public ResponseEntity<ResponseObject> updateCustomerImages(
            @RequestParam("customerId") Integer customerId,
            @RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(new ResponseObject("INVALID_FILES", "No image files provided"));
        }
        try {
            imageStorageService.updateCustomerImages(customerId, files);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Customer images updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("UPDATE_FAILED", e.getMessage()));
        }
    }

    // Cập nhật ảnh Staff (xóa ảnh cũ, thêm ảnh mới)
    @PostMapping("/update/staff")
    public ResponseEntity<ResponseObject> updateStaffImages(
            @RequestParam("staffId") Integer staffId,
            @RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(new ResponseObject("INVALID_FILES", "No image files provided"));
        }
        try {
            imageStorageService.updateStaffImages(staffId, files);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Staff images updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("UPDATE_FAILED", e.getMessage()));
        }
    }
}
