package com.example.restaurantmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class StaffShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "shift_date")
    private LocalDate shiftDate;

    @ElementCollection(targetClass = Shift.class)
    @CollectionTable(name = "staff_shift_shifts", joinColumns = @JoinColumn(name = "shift_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "shift")
    private Set<Shift> shifts; // Lưu nhiều ca cho cùng 1 ngày
    public enum Shift {
        MORNING,
        AFTERNOON,
        EVENING
    }

    public StaffShift() {}

    public StaffShift(Staff staff, LocalDate shiftDate, Set<Shift> shifts) {
        this.staff = staff;
        this.shiftDate = shiftDate;
        this.shifts = shifts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }
    public Set<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Set<Shift> shifts) {
        this.shifts = shifts;
    }
}