package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootPath = Paths.get(System.getProperty("user.dir"), "uploads"); //

    public FileStorageServiceImpl() {
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload root folder", e);
        }
    }

    @Override
    public String saveFile(MultipartFile file, String folderName) {
        try {
            Path targetDir = rootPath.resolve(folderName); // uploads/menu_items
            Files.createDirectories(targetDir);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = targetDir.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filename; // chỉ trả tên file (không có 'staff/' phía trước)
        } catch (IOException e) {
            throw new RuntimeException("Cannot save image: " + e.getMessage(), e);
        }
    }
}
