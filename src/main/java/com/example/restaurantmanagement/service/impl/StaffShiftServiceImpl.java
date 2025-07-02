package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Staff;
import com.example.restaurantmanagement.model.StaffShift;
import com.example.restaurantmanagement.repository.StaffShiftRepository;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.StaffService;
import com.example.restaurantmanagement.service.StaffShiftService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StaffShiftServiceImpl implements StaffShiftService {

    private final StaffShiftRepository staffShiftRepository;
    private final StaffService staffService;

    public StaffShiftServiceImpl(StaffShiftRepository staffShiftRepository,
                                 StaffService staffService) {
        this.staffShiftRepository = staffShiftRepository;
        this.staffService = staffService;
    }

    @Override
    public StaffShift assignShift(StaffShift shift) {
        if (shift.getStaff() == null || shift.getStaff().getId() == null) {
            throw new NVException(ErrorCode.STAFF_ID_NOT_NULL, new Object[]{"Staff is missing or invalid"});
        }

        Staff staff = staffService.getStaffById(shift.getStaff().getId())
                .orElseThrow(() -> new NVException(ErrorCode.STAFF_NOT_FOUND, new Object[]{"ID = " + shift.getStaff().getId()}));

        shift.setStaff(staff);
        return staffShiftRepository.save(shift);
    }

    @Override
    public List<StaffShift> getShiftsByDate(LocalDate date) {
        return staffShiftRepository.findByShiftDate(date);
    }

    @Override
    public List<StaffShift> getShiftsByStaff(Staff staff) {
        return staffShiftRepository.findByStaff(staff);
    }

    @Override
    public List<StaffShift> getShiftsByStaffAndDate(Staff staff, LocalDate date) {
        return staffShiftRepository.findByStaffAndShiftDate(staff, date);
    }

    @Override
    public void deleteShift(Integer id) {
        if (!staffShiftRepository.existsById(id)) {
            throw new NVException(ErrorCode.STAFF_SHIFT_NOT_FOUND, new Object[]{"ID = " + id});
        }
        staffShiftRepository.deleteById(id);
    }

    @Override
    public Optional<StaffShift> getById(Integer id) {
        return staffShiftRepository.findById(id);
    }

    @Override
    public ResponseObject removeShiftType(Integer staffShiftId, String shiftType) {
        Optional<StaffShift> shiftOpt = staffShiftRepository.findById(staffShiftId);

        if (shiftOpt.isEmpty()) {
            throw new NVException(ErrorCode.STAFF_SHIFT_NOT_FOUND, new Object[]{"ID = " + staffShiftId});
        }

        StaffShift shift = shiftOpt.get();

        try {
            boolean removed = shift.getShifts().remove(StaffShift.Shift.valueOf(shiftType.toUpperCase()));
            if (!removed) {
                throw new NVException(ErrorCode.SHIFT_TYPE_NOT_FOUND, new Object[]{"Shift type not assigned: " + shiftType});
            }

            if (shift.getShifts().isEmpty()) {
                staffShiftRepository.deleteById(staffShiftId);
                return new ResponseObject("SUCCESS", "Shift removed and record deleted");
            } else {
                StaffShift updated = staffShiftRepository.save(shift);
                return new ResponseObject(updated);
            }
        } catch (IllegalArgumentException e) {
            throw new NVException(ErrorCode.INVALID_SHIFT_TYPE, new Object[]{"Invalid shift type: " + shiftType});
        }
    }
}
