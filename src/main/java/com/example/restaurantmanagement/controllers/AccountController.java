package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Account;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody Account account) {
        Account created = accountService.createAccount(account);
        return ResponseEntity.ok(new ResponseObject(created));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestParam String username,
                                                @RequestParam String password) {
        Optional<Account> account = accountService.login(username, password);
        if (account.isPresent()) {
            return ResponseEntity.ok(new ResponseObject(account.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObject("AUTH_FAILED", "Invalid username or password"));
        }
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable Integer id) {
        return accountService.findById(id)
                .map(account -> ResponseEntity.ok(new ResponseObject(account)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Account not found")));
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Integer id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Account deleted successfully"));
    }

}
