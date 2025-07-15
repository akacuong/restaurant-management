package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.repository.AccountRepository;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.StaffService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staffs")
public class StaffController {

    private final StaffService staffService;
    private final AccountRepository accountRepository;

    public StaffController(StaffService staffService, AccountRepository accountRepository) {
        this.staffService = staffService;
        this.accountRepository = accountRepository;
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createStaff(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String position,
            @RequestParam String address,
            @RequestParam Integer accountId,
            @RequestParam String dateOfBirth,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        if (!accountRepository.existsById(accountId)) {
            throw new NVException(ErrorCode.ACCOUNT_NOT_FOUND, new Object[]{accountId});
        }

        Staff staff = buildStaffFromRequest(null, name, phone, position, address, accountId, dateOfBirth);
        Staff created = staffService.createStaff(staff, image);
        return ResponseEntity.ok(new ResponseObject(created));
    }
    // UPDATE
    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateStaff(
            @RequestParam Integer id,
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String position,
            @RequestParam String address,
            @RequestParam Integer accountId,
            @RequestParam String dateOfBirth,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        if (!accountRepository.existsById(accountId)) {
            throw new NVException(ErrorCode.ACCOUNT_NOT_FOUND, new Object[]{accountId});
        }

        Staff staff = buildStaffFromRequest(id, name, phone, position, address, accountId, dateOfBirth);
        Staff updated = staffService.updateStaff(staff, image);
        return ResponseEntity.ok(new ResponseObject(updated));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getStaff(@PathVariable Integer id) {
        Staff staff = staffService.getStaffById(id)
                .orElseThrow(() -> new NVException(ErrorCode.STAFF_NOT_FOUND, new Object[]{id}));
        return ResponseEntity.ok(new ResponseObject(staff));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllStaff() {
        return ResponseEntity.ok(new ResponseObject(staffService.getAllStaff()));
    }

    // DELETE
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteStaff(@RequestParam Integer id) {
        Staff staff = staffService.getStaffById(id)
                .orElseThrow(() -> new NVException(ErrorCode.STAFF_NOT_FOUND, new Object[]{id}));
        staffService.deleteStaff(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Staff deleted successfully"));
    }
    //  method để tái sử dụng
    private Staff buildStaffFromRequest(Integer id, String name, String phone, String position,
                                        String address, Integer accountId, String dateOfBirth) {
        Staff staff = new Staff();
        if (id != null) staff.setId(id);
        staff.setName(name);
        staff.setPhone(phone);
        staff.setPosition(position);
        staff.setAddress(address);
        staff.setAccountId(accountId);
        staff.setDateOfBirth(LocalDate.parse(dateOfBirth));
        return staff;
    }
    @GetMapping("/search")
    public ResponseEntity<List<Staff>> searchStaff(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String position) {
        List<Staff> result = staffService.searchStaff(name, phone, address, position);
        return ResponseEntity.ok(result);
    }

}