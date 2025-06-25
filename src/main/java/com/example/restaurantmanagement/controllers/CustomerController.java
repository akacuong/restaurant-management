package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.saveCustomer(customer);
            return ResponseEntity.ok(savedCustomer);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ✅ READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody Customer updatedCustomer) {
        Optional<Customer> existing = customerService.getCustomerById(id);
        if (existing.isPresent()) {
            Customer customer = existing.get();
            customer.setName(updatedCustomer.getName());
            customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
            customer.setEmail(updatedCustomer.getEmail());
            customer.setAddress(updatedCustomer.getAddress());
            return ResponseEntity.ok(customerService.saveCustomer(customer));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {
        Optional<Customer> existing = customerService.getCustomerById(id);
        if (existing.isPresent()) {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok("Deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Customer not found.");
        }
    }
}
