package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.form.AccountLoginForm;
import com.example.restaurantmanagement.model.Account;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.AccountService;
import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ResponseObject> login(@RequestBody AccountLoginForm loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing username or password"});
        }
        Map<String, Object> response = accountService.loginAndGenerateToken(username, password);
        return ResponseEntity.ok(new ResponseObject(response));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable Integer id) {
        return accountService.findById(id)
                .map(account -> ResponseEntity.ok(new ResponseObject(account)))
                .orElseThrow(() -> new NVException(ErrorCode.STAFF_NOT_FOUND));
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> delete(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        if (id == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing account ID"});
        }
        accountService.deleteAccount(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Account deleted successfully"));
    }
    @GetMapping("/search")
    public List<Account> searchAccounts(@RequestParam(required = false) String username,
                                        @RequestParam(required = false) Role role) {
        return accountService.searchAccounts(username, role);
    }
}
