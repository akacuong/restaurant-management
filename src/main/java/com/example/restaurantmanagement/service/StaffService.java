package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    Staff createStaff(Staff staff);
    Staff updateStaff(Staff staff);
    Optional<Staff> getStaffById(Integer id);
    List<Staff> getAllStaff();
    void deleteStaff(Integer id);
}
