package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.Account;
import com.example.restaurantmanagement.repository.AccountRepository;
import com.example.restaurantmanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Optional<Account> login(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password);
    }
    @Override
    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    public void deleteAccount(Integer id) {
        accountRepository.deleteById(id);
    }
}
