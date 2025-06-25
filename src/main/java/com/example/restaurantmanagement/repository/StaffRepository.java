package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Integer> {
}
