package com.example.restaurantmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Staff {

    @Id
    @Column(name = "staff_id") // Có thể đặt lại tên cho rõ ràng
    private Integer id;

    private String name;

    private String phone;

    private String position;

    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToOne
    @MapsId
    @JoinColumn(name = "staff_id") // staff_id vừa là khóa chính vừa là foreign key
    private Account account;

    public Staff() {}

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
