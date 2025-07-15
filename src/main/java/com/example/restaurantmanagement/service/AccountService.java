package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Account;

import javax.management.relation.Role;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AccountService {

    Account createAccount(Account account);
    Optional<Account> findById(Integer id);
    Optional<Account> findByUsername(String username);
    Map<String, Object> loginAndGenerateToken(String username, String password);
    void deleteAccount(Integer id);
    List<Account> searchAccounts(String username, Role role);
}
