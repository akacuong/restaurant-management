package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.repository.StaffRepository;
import com.example.restaurantmanagement.service.StaffService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    // ✅ CREATE
    @Override
    public Staff createStaff(Staff staff) {
        if (staff.getId() != null) {
            throw new IllegalArgumentException("New staff must not have an ID");
        }
        return staffRepository.save(staff);
    }

    // ✅ UPDATE
    @Override
    public Staff updateStaff(Staff updatedStaff) {
        if (updatedStaff.getId() == null) {
            throw new IllegalArgumentException("Staff ID must not be null for update");
        }

        Optional<Staff> existingOpt = staffRepository.findById(updatedStaff.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Staff not found with ID = " + updatedStaff.getId());
        }

        Staff existing = existingOpt.get();
        existing.setName(updatedStaff.getName());
        existing.setPhone(updatedStaff.getPhone());
        existing.setAddress(updatedStaff.getAddress());
        existing.setPosition(updatedStaff.getPosition());
        existing.setDateOfBirth(updatedStaff.getDateOfBirth());
        existing.setAccountId(updatedStaff.getAccountId());

        return staffRepository.save(existing);
    }

    // ✅ READ BY ID
    @Override
    public Optional<Staff> getStaffById(Integer id) {
        return staffRepository.findById(id);
    }

    // ✅ READ ALL
    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    // ✅ DELETE
    @Override
    public void deleteStaff(Integer id) {
        if (!staffRepository.existsById(id)) {
            throw new RuntimeException("Staff not found with ID = " + id);
        }
        staffRepository.deleteById(id);
    }
}
