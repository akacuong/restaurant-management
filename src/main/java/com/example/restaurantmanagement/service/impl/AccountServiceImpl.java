package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.infrastructure.security.JwtTokenUtil;
import com.example.restaurantmanagement.model.Account;
import com.example.restaurantmanagement.repository.AccountRepository;
import com.example.restaurantmanagement.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AccountServiceImpl(AccountRepository accountRepository,
                              PasswordEncoder passwordEncoder,
                              JwtTokenUtil jwtTokenUtil) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @Override
    public Account createAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Username must not be empty"});
        }

        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Password must not be empty"});
        }

        // Kiểm tra username đã tồn tại
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new NVException(ErrorCode.DUPLICATE_ACCOUNT, new Object[]{account.getUsername()});
        }

        // Mã hóa mật khẩu và lưu
        String rawPassword = account.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        account.setPassword(encodedPassword);

        try {
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new NVException(ErrorCode.INTERNAL_ERROR, e, new Object[]{"Failed to save account"});
        }
    }
    // So sánh mật khẩu đã mã hóa khi đăng nhập
    @Override
    public Map<String, Object> loginAndGenerateToken(String username, String password) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);

        if (accountOpt.isEmpty()) {
            throw new NVException(ErrorCode.INVALID_LOGIN, new Object[]{"Invalid username or password"});
        }

        Account account = accountOpt.get();

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new NVException(ErrorCode.INVALID_LOGIN, new Object[]{"Invalid username or password"});
        }

        String token = jwtTokenUtil.generateToken(account);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        responseData.put("username", account.getUsername());
        responseData.put("role", account.getRole());

        return responseData;
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public void deleteAccount(Integer id) {
        if (!accountRepository.existsById(id)) {
            throw new NVException(ErrorCode.USER_NOT_FOUND, new Object[]{id});
        }
        accountRepository.deleteById(id);
    }

    @Override
    public List<Account> searchAccounts(String username, Role role) {
        if ((username == null || username.isBlank()) && role == null) {
            return accountRepository.findAll();
        }
        return accountRepository.searchAccounts(
                (username != null && !username.isBlank()) ? username : null,
                role
        );
    }
}
