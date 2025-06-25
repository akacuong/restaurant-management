package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Account;
import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.repository.AccountRepository;
import com.example.restaurantmanagement.repository.StaffRepository;
import com.example.restaurantmanagement.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public StaffController(StaffService staffService, AccountRepository accountRepository, StaffRepository staffRepository) {
        this.staffService = staffService;
        this.accountRepository = accountRepository;
        this.staffRepository = staffRepository;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<?> createStaff(@RequestBody Staff staff) {
        Integer accId = staff.getAccount().getAccountId();

        Account account = accountRepository.findById(accId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        staff.setAccount(account); // Quan trọng
        Staff savedStaff = staffRepository.save(staff);

        return ResponseEntity.ok(savedStaff);
    }

    // ✅ READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getStaff(@PathVariable Integer id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        return staff.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Integer id, @RequestBody Staff updatedStaff) {
        Optional<Staff> optional = staffService.getStaffById(id);
        if (optional.isPresent()) {
            Staff staff = optional.get();
            staff.setName(updatedStaff.getName());
            staff.setPhone(updatedStaff.getPhone());
            staff.setPosition(updatedStaff.getPosition());
            staff.setAddress(updatedStaff.getAddress());
            staff.setDateOfBirth(updatedStaff.getDateOfBirth());
            return ResponseEntity.ok(staffService.saveStaff(staff));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable Integer id) {
        Optional<Staff> optional = staffService.getStaffById(id);
        if (optional.isPresent()) {
            staffService.deleteStaff(id);
            return ResponseEntity.ok("Staff deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Staff not found.");
        }
    }
}
