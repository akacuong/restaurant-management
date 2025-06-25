package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Account;

import java.util.Optional;

public interface AccountService {

    Account createAccount(Account account);

    Optional<Account> login(String username, String password);

    Optional<Account> findById(Integer id);

    void deleteAccount(Integer id);
}
