package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.repository.StaffRepository;
import com.example.restaurantmanagement.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public Optional<Staff> getStaffById(Integer id) {
        return staffRepository.findById(id);
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public void deleteStaff(Integer id) {
        staffRepository.deleteById(id);
    }
}
