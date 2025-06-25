package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Reservation;
import com.example.restaurantmanagement.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation saved = reservationService.saveReservation(reservation);
        return ResponseEntity.ok(saved);
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Integer id) {
        Optional<Reservation> res = reservationService.getReservationById(id);
        return res.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Integer id, @RequestBody Reservation updated) {
        Optional<Reservation> optional = reservationService.getReservationById(id);
        if (optional.isPresent()) {
            Reservation res = optional.get();
            res.setTime(updated.getTime());
            res.setNote(updated.getNote());
            res.setNumberOfPeople(updated.getNumberOfPeople());
            res.setStatus(updated.getStatus());
            res.setCustomer(updated.getCustomer());
            return ResponseEntity.ok(reservationService.saveReservation(res));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) {
        if (reservationService.getReservationById(id).isPresent()) {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok("Deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Reservation not found.");
        }
    }
}
