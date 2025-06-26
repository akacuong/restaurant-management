package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);           // ✅ CREATE

    Reservation updateReservation(Reservation reservation);           // ✅ UPDATE

    Optional<Reservation> getReservationById(Integer id);            // ✅ READ ONE

    List<Reservation> getAllReservations();                          // ✅ READ ALL

    void deleteReservation(Integer id);                              // ✅ DELETE
}
