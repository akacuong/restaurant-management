package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Integer> {
    @Query("SELECT s FROM Staff s " +
            "WHERE (:name IS NOT NULL AND LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "OR (:phone IS NOT NULL AND s.phone LIKE CONCAT('%', :phone, '%')) " +
            "OR (:address IS NOT NULL AND LOWER(s.address) LIKE LOWER(CONCAT('%', :address, '%'))) " +
            "OR (:position IS NOT NULL AND LOWER(s.position) LIKE LOWER(CONCAT('%', :position, '%')))")
    List<Staff> searchStaff(@Param("name") String name,
                            @Param("phone") String phone,
                            @Param("address") String address,
                            @Param("position") String position);
}
