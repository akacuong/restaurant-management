package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.ImageStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageStorageRepository extends JpaRepository<ImageStorage, Long> {
    List<ImageStorage> findByCustomerId(Integer customerId);
    List<ImageStorage> findByStaffId(Integer staffId);
    void deleteByCustomerId(Integer customerId);
    void deleteByStaffId(Integer staffId);

}
