package com.example.restaurantmanagement.controllers;

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

        try {
            if (!accountRepository.existsById(accountId)) {
                return ResponseEntity.badRequest()
                        .body(new ResponseObject("NOT_FOUND", "Account not found with ID = " + accountId));
            }

            Staff staff = new Staff();
            staff.setName(name);
            staff.setPhone(phone);
            staff.setPosition(position);
            staff.setAddress(address);
            staff.setAccountId(accountId);
            staff.setDateOfBirth(LocalDate.parse(dateOfBirth));

            Staff created = staffService.createStaff(staff, image);
            return ResponseEntity.ok(new ResponseObject(created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("CREATE_FAILED", e.getMessage()));
        }
    }

    // UPDATE (form-data)
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

        try {
            if (!accountRepository.existsById(accountId)) {
                return ResponseEntity.badRequest()
                        .body(new ResponseObject("NOT_FOUND", "Account not found with ID = " + accountId));
            }
            Staff staff = new Staff();
            staff.setId(id);
            staff.setName(name);
            staff.setPhone(phone);
            staff.setPosition(position);
            staff.setAddress(address);
            staff.setAccountId(accountId);
            staff.setDateOfBirth(LocalDate.parse(dateOfBirth));
            Staff updated = staffService.updateStaff(staff, image);
            return ResponseEntity.ok(new ResponseObject(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("UPDATE_FAILED", e.getMessage()));
        }
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getStaff(@PathVariable Integer id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        return staff.map(value -> ResponseEntity.ok(new ResponseObject(value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Staff not found")));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(new ResponseObject(staffList));
    }

    // DELETE
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteStaff(@RequestParam Integer id) {
        Optional<Staff> existing = staffService.getStaffById(id);
        if (existing.isPresent()) {
            staffService.deleteStaff(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Staff deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", "Staff not found"));
        }
    }
}
