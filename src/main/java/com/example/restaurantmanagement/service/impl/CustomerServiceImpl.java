package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.repository.AccountRepository;
import com.example.restaurantmanagement.repository.CustomerRepository;
import com.example.restaurantmanagement.service.CustomerService;
import com.example.restaurantmanagement.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final FileStorageService fileStorageService;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               AccountRepository accountRepository,
                               FileStorageService fileStorageService) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Customer createCustomer(Customer customer, MultipartFile imageFile) {
        Integer accountId = customer.getAccountId();

        if (accountId == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Account ID must not be null"});
        }

        boolean accountExists = accountRepository.existsById(accountId);
        if (!accountExists) {
            throw new NVException(ErrorCode.USER_NOT_FOUND, new Object[]{"Account ID = " + accountId});
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveFile(imageFile, "customer");
            customer.setProfileImage(imagePath);
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer updatedCustomer, MultipartFile imageFile) {
        if (updatedCustomer.getId() == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Customer ID must not be null"});
        }

        Optional<Customer> existingOpt = customerRepository.findById(updatedCustomer.getId());
        if (existingOpt.isEmpty()) {
            throw new NVException(ErrorCode.CUSTOMER_NOT_FOUND, new Object[]{updatedCustomer.getId()});
        }

        Customer existing = existingOpt.get();
        existing.setName(updatedCustomer.getName());
        existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setAddress(updatedCustomer.getAddress());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveFile(imageFile, "customer");
            existing.setProfileImage(imagePath);
        }

        return customerRepository.save(existing);
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteCustomer(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new NVException(ErrorCode.CUSTOMER_NOT_FOUND, new Object[]{id});
        }
        customerRepository.deleteById(id);
    }
    @Override
    public List<Customer> searchCustomers(String name, String address, String phone) {
        boolean isNameEmpty = (name == null || name.trim().isEmpty());
        boolean isAddressEmpty = (address == null || address.trim().isEmpty());
        boolean isPhoneEmpty = (phone == null || phone.trim().isEmpty());

        if (isNameEmpty && isAddressEmpty && isPhoneEmpty) {
            return customerRepository.findAll();
        }

        return customerRepository.searchCustomers(
                !isNameEmpty ? name.trim() : null,
                !isAddressEmpty ? address.trim() : null,
                !isPhoneEmpty ? phone.trim() : null
        );
    }

}
