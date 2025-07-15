package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c " +
            "WHERE (:name IS NOT NULL AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "OR (:address IS NOT NULL AND LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))) " +
            "OR (:phone IS NOT NULL AND c.phoneNumber LIKE CONCAT('%', :phone, '%'))")
    List<Customer> searchCustomers(@Param("name") String name,
                                                @Param("address") String address,
                                                @Param("phone") String phone);

}
