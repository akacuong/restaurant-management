package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.infrastructure.dto.ReservationTimeStatDTO;
import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Reservation;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createReservation(@RequestBody Reservation reservation) {
        if (reservation == null || reservation.getTableInfos() == null || reservation.getTableInfos().isEmpty()
                || reservation.getStartTime() == null || reservation.getEndTime() == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing table list or time"});
        }
        Reservation saved = reservationService.createReservation(reservation);
        return ResponseEntity.ok(new ResponseObject(saved));
    }
    // READ ALL WITH PAGINATION
    @GetMapping
    public ResponseEntity<ResponseObject> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationService.getAllReservations(pageable);
        return ResponseEntity.ok(new ResponseObject(reservations));
    }
    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getReservation(@PathVariable Integer id) {
        Reservation reservation = reservationService.getReservationById(id)
                .orElseThrow(() -> new NVException(ErrorCode.RESERVATION_NOT_FOUND));
        return ResponseEntity.ok(new ResponseObject(reservation));
    }

    // UPDATE
    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateReservation(@RequestBody Integer id,
                                                            @RequestBody Reservation updated) {
        if (updated == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing reservation data"});
        }

        updated.setId(id);
        Reservation result = reservationService.updateReservation(updated);
        return ResponseEntity.ok(new ResponseObject(result));
    }

    // DELETE
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteReservation(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        if (id == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing reservation ID"});
        }
        Reservation reservation = reservationService.getReservationById(id)
                .orElseThrow(() -> new NVException(ErrorCode.RESERVATION_NOT_FOUND));

        reservationService.deleteReservation(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Reservation deleted successfully"));
    }
    @GetMapping("/stats/by-day-and-hour")
    public ResponseEntity<ResponseObject> getReservationCountByDayAndHour() {
        List<ReservationTimeStatDTO> stats = reservationService.countReservationsByDayAndHour();
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Reservation count by day and hour", stats));
    }
}
