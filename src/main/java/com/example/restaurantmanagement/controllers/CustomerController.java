package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ✅ CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createCustomer(@RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity.ok(new ResponseObject(createdCustomer));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("CREATE_FAILED", ex.getMessage()));
        }
    }

    // ✅ READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCustomer(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(value -> ResponseEntity.ok(new ResponseObject(value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Customer not found")));
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(new ResponseObject(customers));
    }

    // ✅ UPDATE
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateCustomer(@PathVariable Integer id,
                                                         @RequestBody Customer customer) {
        try {
            customer.setId(id); // Gán ID từ path vào object
            Customer updatedCustomer = customerService.updateCustomer(customer);
            return ResponseEntity.ok(new ResponseObject(updatedCustomer));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("UPDATE_FAILED", ex.getMessage()));
        }
    }

    // ✅ DELETE (dùng POST để delete)
    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteCustomer(@PathVariable Integer id) {
        Optional<Customer> existing = customerService.getCustomerById(id);
        if (existing.isPresent()) {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Customer deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", "Customer not found"));
        }
    }
}
