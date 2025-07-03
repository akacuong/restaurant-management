package com.example.restaurantmanagement.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveFile(MultipartFile file, String folderName);
}
