package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {

    // ✅ CREATE
    Staff createStaff(Staff staff);

    // ✅ UPDATE
    Staff updateStaff(Staff staff);

    // ✅ READ ONE
    Optional<Staff> getStaffById(Integer id);

    // ✅ READ ALL
    List<Staff> getAllStaff();

    // ✅ DELETE
    void deleteStaff(Integer id);
}
