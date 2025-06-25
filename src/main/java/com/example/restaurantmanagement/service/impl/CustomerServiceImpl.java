package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.Account;
import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.repository.AccountRepository;
import com.example.restaurantmanagement.repository.CustomerRepository;
import com.example.restaurantmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
    }
    @Override
    public Customer saveCustomer(Customer customer) {
        // Kiểm tra xem Account có tồn tại không
        Integer accId = customer.getAccount().getAccountId();

        Account account = accountRepository.findById(accId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        customer.setAccount(account); // rất quan trọng
        return customerRepository.save(customer);
    }
    @Override
    public Optional<Customer> getCustomerById(Integer id){
        return customerRepository.findById(id);
    }
    @Override
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
    @Override
    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }

}
