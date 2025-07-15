package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.ImageStorage;
import com.example.restaurantmanagement.repository.ImageStorageRepository;
import com.example.restaurantmanagement.service.FileStorageService;
import com.example.restaurantmanagement.service.ImageStorageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private final ImageStorageRepository imageStorageRepository;
    private final FileStorageService fileStorageService;

    public ImageStorageServiceImpl(ImageStorageRepository imageStorageRepository,
                                   FileStorageService fileStorageService) {
        this.imageStorageRepository = imageStorageRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void uploadCustomerImages(Integer customerId, MultipartFile[] files) {
        for (MultipartFile file : files) {
            String path = fileStorageService.saveFile(file, "customer");
            ImageStorage image = new ImageStorage();
            image.setCustomerId(customerId);
            image.setImagePath(path);
            imageStorageRepository.save(image);
        }
    }

    @Override
    public void uploadStaffImages(Integer staffId, MultipartFile[] files) {
        for (MultipartFile file : files) {
            String path = fileStorageService.saveFile(file, "staff");
            ImageStorage image = new ImageStorage();
            image.setStaffId(staffId);
            image.setImagePath(path);
            imageStorageRepository.save(image);
        }
    }
    @Override
    @Transactional
    public void updateCustomerImages(Integer customerId, MultipartFile[] files) {
        imageStorageRepository.deleteByCustomerId(customerId);
        uploadCustomerImages(customerId, files);
    }

    @Override
    @Transactional
    public void updateStaffImages(Integer staffId, MultipartFile[] files) {
        imageStorageRepository.deleteByStaffId(staffId);
        uploadStaffImages(staffId, files);
    }

}
