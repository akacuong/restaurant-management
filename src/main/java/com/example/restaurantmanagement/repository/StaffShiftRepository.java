package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.StaffShift;
import com.example.restaurantmanagement.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT s FROM StaffShift s " +
            "WHERE (:name IS NULL OR LOWER(s.staff.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:shift IS NULL OR :shift IN elements(s.shifts))")
    List<StaffShift> findByStaffNameAndShift(@Param("name") String name,
                                             @Param("shift") StaffShift.Shift shift);
}
