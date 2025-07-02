package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.repository.AccountRepository;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<ResponseObject> createStaff(@RequestBody Staff staff) {
        try {
            if (staff.getAccountId() == null) {
                return ResponseEntity.badRequest()
                        .body(new ResponseObject("VALIDATION_FAILED", "Account ID must not be null"));
            }

            if (!accountRepository.existsById(staff.getAccountId())) {
                return ResponseEntity.badRequest()
                        .body(new ResponseObject("NOT_FOUND", "Account not found with ID = " + staff.getAccountId()));
            }

            Staff created = staffService.createStaff(staff);
            return ResponseEntity.ok(new ResponseObject(created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("CREATE_FAILED", e.getMessage()));
        }
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getStaff(@PathVariable Integer id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        return staff.map(value -> ResponseEntity.ok(new ResponseObject(value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Staff not found")));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(new ResponseObject(staffList));
    }

    // UPDATE
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateStaff(@PathVariable Integer id,
                                                      @RequestBody Staff staff) {
        try {
            staff.setId(id); // đảm bảo ID từ path truyền vào object
            Staff updated = staffService.updateStaff(staff);
            return ResponseEntity.ok(new ResponseObject(updated));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("UPDATE_FAILED", ex.getMessage()));
        }
    }

    // DELETE
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteStaff(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        if (id == null) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("INVALID_REQUEST", "Missing 'id' in request body"));
        }

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
