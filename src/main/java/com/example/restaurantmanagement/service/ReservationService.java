package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);
    Reservation updateReservation(Reservation reservation);
    Optional<Reservation> getReservationById(Integer id);
    Page<Reservation> getAllReservations(Pageable pageable);
    void deleteReservation(Integer id);

}
