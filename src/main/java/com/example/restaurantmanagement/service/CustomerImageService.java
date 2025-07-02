package com.example.restaurantmanagement.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerImageService {
    String uploadProfileImage(Integer customerId, MultipartFile profileImage);
    List<String> uploadGalleryImages(Integer customerId, List<MultipartFile> galleryImages);
}
