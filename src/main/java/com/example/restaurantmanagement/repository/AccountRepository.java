package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query(value = "SELECT * FROM account WHERE username = :username AND password = :password", nativeQuery = true)
    Optional<Account> findByUsernameAndPassword(@Param("username") String username,
                                                @Param("password") String password);
}
