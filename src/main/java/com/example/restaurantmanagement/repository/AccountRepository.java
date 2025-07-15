package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query(value="SELECT * FROM account WHERE username= :username", nativeQuery = true)
    Optional<Account> findByUsername(@Param("username") String username);
    @Query("SELECT a FROM Account a " +
            "WHERE (:username IS NOT NULL AND LOWER(a.username) LIKE LOWER(CONCAT('%', :username, '%'))) " +
            "OR (:role IS NOT NULL AND a.role = :role)")
    List<Account> searchAccounts(@Param("username") String username,
                                 @Param("role") Role role);
}
