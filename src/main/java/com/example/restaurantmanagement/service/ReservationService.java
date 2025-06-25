package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Reservation saveReservation(Reservation reservation);

    Optional<Reservation> getReservationById(Integer id);

    List<Reservation> getAllReservations();

    void deleteReservation(Integer id);
}
