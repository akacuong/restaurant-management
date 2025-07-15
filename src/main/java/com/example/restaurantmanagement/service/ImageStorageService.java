package com.example.restaurantmanagement.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    void uploadCustomerImages(Integer customerId, MultipartFile[] files);
    void uploadStaffImages(Integer staffId, MultipartFile[] files);
    void updateCustomerImages(Integer customerId, MultipartFile[] files);
    void updateStaffImages(Integer staffId, MultipartFile[] files);

}
