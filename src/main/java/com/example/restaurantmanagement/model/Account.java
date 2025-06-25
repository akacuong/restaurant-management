package com.example.restaurantmanagement.model;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    private String username;

    private String password;

    private String role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Customer customer;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Staff staff;

    public Account() {}

    // Getters and Setters
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
