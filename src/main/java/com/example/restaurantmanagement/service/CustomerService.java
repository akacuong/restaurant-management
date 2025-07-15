package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Customer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer createCustomer(Customer customer, MultipartFile imageFile);
    Customer updateCustomer(Customer customer, MultipartFile imageFile);
    Optional<Customer> getCustomerById(Integer id);
    List<Customer> getAllCustomers();
    void deleteCustomer(Integer id);
    List<Customer> searchCustomers(String name, String province, String phone);
}