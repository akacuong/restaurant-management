package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.repository.AccountRepository;
import com.example.restaurantmanagement.repository.CustomerRepository;
import com.example.restaurantmanagement.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Integer accountId = customer.getAccountId();

        if (accountId == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Account ID must not be null"});
        }

        boolean accountExists = accountRepository.existsById(accountId);
        if (!accountExists) {
            throw new NVException(ErrorCode.USER_NOT_FOUND, new Object[]{"Account ID = " + accountId});
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer updatedCustomer) {
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
}
