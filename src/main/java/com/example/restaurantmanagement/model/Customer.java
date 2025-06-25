package com.example.restaurantmanagement.model;

import jakarta.persistence.*;

@Entity
public class Customer {
    @Id
    private Integer customerId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id")
    private Account account;

    private String name;

    @Column(name = "phonenumber")
    private String phoneNumber;

    private String email;

    private String address;
    public Customer() {}

    // Getters and Setters

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
