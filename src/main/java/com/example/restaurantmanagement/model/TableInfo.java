package com.example.restaurantmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "table_info")
public class TableInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "table_number", nullable = false)
    private String tableNumber;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private TableStatus status;

    public TableInfo() {}
    public enum TableStatus {
        EMPTY,
        RESERVED,
        OCCUPIED
    }
    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
}
