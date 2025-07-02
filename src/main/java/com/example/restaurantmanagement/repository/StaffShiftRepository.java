package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.StaffShift;
import com.example.restaurantmanagement.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffShiftRepository extends JpaRepository<StaffShift, Integer> {

    // Lấy tất cả ca làm của 1 nhân viên theo ngày
    List<StaffShift> findByStaffAndShiftDate(Staff staff, LocalDate shiftDate);

    // Lấy tất cả ca làm theo ngày
    List<StaffShift> findByShiftDate(LocalDate shiftDate);

    // Lấy ca làm theo nhân viên
    List<StaffShift> findByStaff(Staff staff);
}
