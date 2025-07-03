package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Staff;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    Staff createStaff(Staff staff, MultipartFile imageFile);
    Staff updateStaff(Staff staff, MultipartFile imageFile);
    Optional<Staff> getStaffById(Integer id);
    List<Staff> getAllStaff();
    void deleteStaff(Integer id);
}
