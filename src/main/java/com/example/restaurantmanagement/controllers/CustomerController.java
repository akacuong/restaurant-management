package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.CustomerImageService;
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
    private final CustomerImageService customerImageService;

    public CustomerController(CustomerService customerService, CustomerImageService customerImageService) {
        this.customerService = customerService;
        this.customerImageService = customerImageService;
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createCustomer(@RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity.ok(new ResponseObject(createdCustomer));
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
    // Upload 1 ảnh đại diện
    @PostMapping("/{id}/upload-profile")
    public ResponseEntity<ResponseObject> uploadProfile(@PathVariable Integer id,
                                                        @RequestParam("file") MultipartFile file) {
        try {
            // Trả về tên file đã lưu trong DB
            String fileName = customerImageService.uploadProfileImage(id, file);

            // Ghép URL trả về cho frontend hiển thị ảnh
            String imageUrl = "/uploads/customers/profile/" + fileName;

            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Profile image uploaded", imageUrl));
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("UPLOAD_FAILED", ex.getMessage()));
        }
    }
    // Upload nhiều ảnh gallery
    @PostMapping("/{id}/upload-gallery")
    public ResponseEntity<ResponseObject> uploadGallery(@PathVariable Integer id,
                                                        @RequestParam("files") List<MultipartFile> files) {
        try {
            // Upload và chỉ nhận về danh sách tên file
            List<String> fileNames = customerImageService.uploadGalleryImages(id, files);

            // Ghép đường dẫn đầy đủ để frontend có thể hiển thị ảnh
            List<String> urls = fileNames.stream()
                    .map(name -> "/uploads/customers/gallery/" + name)  // dùng relative URL
                    .toList();

            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Uploaded " + urls.size() + " images", urls));

        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("UPLOAD_FAILED", ex.getMessage()));
        }
    }

}
