package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.repository.StaffRepository;
import com.example.restaurantmanagement.service.FileStorageService;
import com.example.restaurantmanagement.service.StaffService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final FileStorageService fileStorageService;

    public StaffServiceImpl(StaffRepository staffRepository, FileStorageService fileStorageService) {
        this.staffRepository = staffRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Staff createStaff(Staff staff, MultipartFile imageFile) {
        if (staff.getId() != null) {
            throw new NVException(ErrorCode.STAFF_ID_NOT_NULL, new Object[]{"New staff must not have an ID"});
        }
        // Xử lý ảnh nếu có
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveFile(imageFile, "staff");
            staff.setImage(imagePath);
        }

        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Staff updatedStaff, MultipartFile imageFile) {
        if (updatedStaff.getId() == null) {
            throw new NVException(ErrorCode.STAFF_ID_NOT_NULL, new Object[]{"Staff ID must not be null for update"});
        }

        Optional<Staff> existingOpt = staffRepository.findById(updatedStaff.getId());
        if (existingOpt.isEmpty()) {
            throw new NVException(ErrorCode.STAFF_NOT_FOUND, new Object[]{"ID = " + updatedStaff.getId()});
        }

        Staff existing = existingOpt.get();

        // Cập nhật thông tin cơ bản
        existing.setName(updatedStaff.getName());
        existing.setPhone(updatedStaff.getPhone());
        existing.setAddress(updatedStaff.getAddress());
        existing.setPosition(updatedStaff.getPosition());
        existing.setDateOfBirth(updatedStaff.getDateOfBirth());
        existing.setAccountId(updatedStaff.getAccountId());

        // Nếu có ảnh mới thì cập nhật
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveFile(imageFile, "staff");
            existing.setImage(imagePath);
        }

        return staffRepository.save(existing);
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
        if (!staffRepository.existsById(id)) {
            throw new NVException(ErrorCode.STAFF_NOT_FOUND, new Object[]{"ID = " + id});
        }
        staffRepository.deleteById(id);
    }
}
