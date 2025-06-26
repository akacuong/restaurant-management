package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Reservation;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ✅ CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createReservation(@RequestBody Reservation reservation) {
        try {
            Reservation saved = reservationService.createReservation(reservation);
            return ResponseEntity.ok(new ResponseObject(saved));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ResponseObject("CREATE_FAILED", ex.getMessage()));
        }
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllReservations() {
        List<Reservation> list = reservationService.getAllReservations();
        return ResponseEntity.ok(new ResponseObject(list));
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getReservation(@PathVariable Integer id) {
        Optional<Reservation> res = reservationService.getReservationById(id);
        return res.map(r -> ResponseEntity.ok(new ResponseObject(r)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Reservation not found")));
    }

    // ✅ UPDATE
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateReservation(@PathVariable Integer id,
                                                            @RequestBody Reservation updated) {
        try {
            updated.setId(id);
            Reservation result = reservationService.updateReservation(updated);
            return ResponseEntity.ok(new ResponseObject(result));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("UPDATE_FAILED", ex.getMessage()));
        }
    }

    // ✅ DELETE
    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteReservation(@PathVariable Integer id) {
        Optional<Reservation> existing = reservationService.getReservationById(id);
        if (existing.isPresent()) {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Reservation deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("NOT_FOUND", "Reservation not found"));
        }
    }
}
