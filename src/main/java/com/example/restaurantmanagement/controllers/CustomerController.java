package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createCustomer(@RequestParam("accountId") Integer accountId,
                                                         @RequestParam("name") String name,
                                                         @RequestParam("phoneNumber") String phoneNumber,
                                                         @RequestParam("email") String email,
                                                         @RequestParam("address") String address,
                                                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Customer customer = new Customer();
            customer.setAccountId(accountId);
            customer.setName(name);
            customer.setPhoneNumber(phoneNumber);
            customer.setEmail(email);
            customer.setAddress(address);

            Customer created = customerService.createCustomer(customer, imageFile);
            return ResponseEntity.ok(new ResponseObject(created));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("CREATE_FAILED", ex.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCustomer(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(value -> ResponseEntity.ok(new ResponseObject(value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Customer not found")));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(new ResponseObject(customers));
    }

    // UPDATE
    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateCustomer(@RequestParam Integer id,
                                                         @RequestParam("name") String name,
                                                         @RequestParam("phoneNumber") String phoneNumber,
                                                         @RequestParam("email") String email,
                                                         @RequestParam("address") String address,
                                                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Customer customer = new Customer();
            customer.setId(id);
            customer.setName(name);
            customer.setPhoneNumber(phoneNumber);
            customer.setEmail(email);
            customer.setAddress(address);

            Customer updated = customerService.updateCustomer(customer, imageFile);
            return ResponseEntity.ok(new ResponseObject(updated));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("UPDATE_FAILED", ex.getMessage()));
        }
    }


    // DELETE
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteCustomer(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        Optional<Customer> existing = customerService.getCustomerById(id);

        if (existing.isPresent()) {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Customer deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", "Customer not found"));
        }
    }
    @GetMapping("/search")
    public List<Customer> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone
    ) {
        return customerService.searchCustomers(name, address, phone);
    }
}
