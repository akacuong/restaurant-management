package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.form.AccountLoginForm;
import com.example.restaurantmanagement.model.Account;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
        try {
            Account created = accountService.createAccount(account);
            return ResponseEntity.ok(new ResponseObject(created));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("VALIDATION_FAILED", ex.getMessage()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseObject("CREATE_FAILED", ex.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody AccountLoginForm loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("INVALID_REQUEST", "Missing username or password"));
        }

        try {
            Map<String, Object> response = accountService.loginAndGenerateToken(username, password);
            return ResponseEntity.ok(new ResponseObject(response));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObject("AUTH_FAILED", ex.getMessage()));
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable Integer id) {
        return accountService.findById(id)
                .map(account -> ResponseEntity.ok(new ResponseObject(account)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Account not found")));
    }
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> delete(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        accountService.deleteAccount(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Account deleted successfully"));
    }

}
