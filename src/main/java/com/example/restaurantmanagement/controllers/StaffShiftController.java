package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.model.StaffShift;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.StaffService;
import com.example.restaurantmanagement.service.StaffShiftService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/api/staff-shifts")
public class StaffShiftController {

    private final StaffShiftService staffShiftService;
    private final StaffService staffService;

    public StaffShiftController(StaffShiftService staffShiftService, StaffService staffService) {
        this.staffShiftService = staffShiftService;
        this.staffService = staffService;
    }
    // Gán ca làm việc
    @PostMapping("/assign")
    public ResponseEntity<ResponseObject> assignShift(@RequestBody StaffShift shift) {
        try {
            StaffShift assigned = staffShiftService.assignShift(shift);
            return ResponseEntity.ok(new ResponseObject(assigned));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("ASSIGN_FAILED", e.getMessage()));
        }
    }
    // Lấy theo ngày
    @GetMapping("/by-date")
    public ResponseEntity<ResponseObject> getShiftsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<StaffShift> shifts = staffShiftService.getShiftsByDate(date);
        return ResponseEntity.ok(new ResponseObject(shifts));
    }
    // Lấy theo nhân viên
    @GetMapping("/by-staff")
    public ResponseEntity<ResponseObject> getShiftsByStaff(@RequestParam Integer staffId) {
        Optional<Staff> staffOpt = staffService.getStaffById(staffId);
        if (staffOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("NOT_FOUND", "Staff not found"));
        }

        List<StaffShift> shifts = staffShiftService.getShiftsByStaff(staffOpt.get());
        return ResponseEntity.ok(new ResponseObject(shifts));
    }
    // Lấy theo nhân viên và ngày
    @GetMapping("/by-staff-and-date")
    public ResponseEntity<ResponseObject> getShiftsByStaffAndDate(@RequestParam Integer staffId,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Optional<Staff> staffOpt = staffService.getStaffById(staffId);
        if (staffOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("NOT_FOUND", "Staff not found"));
        }

        List<StaffShift> shifts = staffShiftService.getShiftsByStaffAndDate(staffOpt.get(), date);
        return ResponseEntity.ok(new ResponseObject(shifts));
    }
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteShift(@RequestBody Integer id) {
        try {
            staffShiftService.deleteShift(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Shift deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("DELETE_FAILED", e.getMessage()));
        }
    }
    @PostMapping("/remove-shift-type")
    public ResponseEntity<ResponseObject> removeShiftType(@RequestBody Map<String, Object> request) {
        try {
            Integer staffShiftId = (Integer) request.get("staffShiftId");
            String shiftType = (String) request.get("shiftType");

            ResponseObject response = staffShiftService.removeShiftType(staffShiftId, shiftType);

            if ("SUCCESS".equals(response.getErrorCode())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("BAD_REQUEST", "Invalid request format"));
        }
    }
    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchShifts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) StaffShift.Shift shift
    ) {
        List<StaffShift> results = staffShiftService.filterShiftsByShiftAndStaffName(name, shift);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Filtered shifts", results));
    }


}
