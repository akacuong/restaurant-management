package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.repository.CustomerRepository;
import com.example.restaurantmanagement.service.CustomerImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CustomerImageServiceImpl implements CustomerImageService {

    private final CustomerRepository customerRepository;

    public CustomerImageServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public String uploadProfileImage(Integer customerId, MultipartFile profileImage) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NVException(ErrorCode.CUSTOMER_NOT_FOUND, new Object[]{customerId}));

        try {
            String originalFilename = profileImage.getOriginalFilename();
            String filename = UUID.randomUUID() + "_" + originalFilename;

            String uploadDir = System.getProperty("user.dir") + "/uploads/customers/profile/";
            File destFile = new File(uploadDir + filename);
            destFile.getParentFile().mkdirs();

            profileImage.transferTo(destFile);

            customer.setProfileImage(filename);
            customerRepository.save(customer);

            return filename;

        } catch (IOException e) {
            throw new NVException(ErrorCode.INTERNAL_ERROR, e, new Object[]{"Failed to upload profile image"});
        }
    }

    @Override
    public List<String> uploadGalleryImages(Integer customerId, List<MultipartFile> galleryImages) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NVException(ErrorCode.CUSTOMER_NOT_FOUND, new Object[]{customerId}));

        List<String> fileNames = new ArrayList<>();
        String uploadDir = System.getProperty("user.dir") + "/uploads/customers/gallery/";

        for (MultipartFile file : galleryImages) {
            if (!file.isEmpty()) {
                try {
                    String originalFilename = file.getOriginalFilename();
                    String filename = UUID.randomUUID() + "_" + originalFilename;
                    File dest = new File(uploadDir + filename);
                    dest.getParentFile().mkdirs();
                    file.transferTo(dest);

                    fileNames.add(filename);

                } catch (IOException e) {
                    throw new NVException(ErrorCode.INTERNAL_ERROR, e, new Object[]{"Failed to upload gallery image"});
                }
            }
        }

        List<String> currentGallery = customer.getGalleryImages();
        if (currentGallery == null) currentGallery = new ArrayList<>();

        currentGallery.addAll(fileNames);
        customer.setGalleryImages(currentGallery);
        customerRepository.save(customer);

        return fileNames;
    }
}
