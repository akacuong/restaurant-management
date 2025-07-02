package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.model.StaffShift;
import com.example.restaurantmanagement.response.ResponseObject;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StaffShiftService {
    StaffShift assignShift(StaffShift shift);

    List<StaffShift> getShiftsByDate(LocalDate date);

    List<StaffShift> getShiftsByStaff(Staff staff);

    List<StaffShift> getShiftsByStaffAndDate(Staff staff, LocalDate date);

    void deleteShift(Integer id);
    Optional<StaffShift> getById(Integer id);
    ResponseObject removeShiftType(Integer staffShiftId, String shiftType);

}
