package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);

    Optional<Customer> getCustomerById(Integer id);
    List<Customer> getAllCustomers();
    void deleteCustomer(Integer id);
}
