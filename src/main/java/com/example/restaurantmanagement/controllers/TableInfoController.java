package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.TableInfo;
import com.example.restaurantmanagement.model.Reservation;
import com.example.restaurantmanagement.service.ReservationService;
import com.example.restaurantmanagement.service.TableInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
public class TableInfoController {

    private final TableInfoService tableInfoService;
    private final ReservationService reservationService;

    @Autowired
    public TableInfoController(TableInfoService tableInfoService, ReservationService reservationService) {
        this.tableInfoService = tableInfoService;
        this.reservationService = reservationService;
    }

    //  CREATE
    @PostMapping
    public ResponseEntity<?> createTable(@RequestBody TableInfo tableInfo) {
        try {
            // Nếu có reservation được gửi kèm
            if (tableInfo.getReservation() != null) {
                Integer reservationId = tableInfo.getReservation().getReservationId();

                if (reservationId != null) {
                    Optional<Reservation> resOpt = reservationService.getReservationById(reservationId);
                    if (resOpt.isPresent()) {
                        tableInfo.setReservation(resOpt.get());
                    } else {
                        return ResponseEntity.badRequest().body("Reservation with ID " + reservationId + " not found.");
                    }
                } else {
                    // Nếu reservation object có nhưng thiếu ID
                    tableInfo.setReservation(null); // Cho phép null thay vì báo lỗi
                }
            } else {
                tableInfo.setReservation(null); // Rõ ràng: nếu không có thì set null luôn
            }

            // Lưu thông tin bàn
            TableInfo saved = tableInfoService.saveTableInfo(tableInfo);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred while creating table: " + e.getMessage());
        }
    }


    //  GET ALL
    @GetMapping
    public ResponseEntity<List<TableInfo>> getAllTables() {
        return ResponseEntity.ok(tableInfoService.getAllTableInfos());
    }

    //  GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTable(@PathVariable Integer id) {
        Optional<TableInfo> table = tableInfoService.getTableInfoById(id);
        return table.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTable(@PathVariable Integer id, @RequestBody TableInfo updated) {
        Optional<TableInfo> tableOpt = tableInfoService.getTableInfoById(id);
        if (tableOpt.isPresent()) {
            TableInfo table = tableOpt.get();
            table.setTableNumber(updated.getTableNumber());
            table.setCapacity(updated.getCapacity());
            table.setStatus(updated.getStatus());

            if (updated.getReservation() != null && updated.getReservation().getReservationId() != null) {
                Optional<Reservation> resOpt = reservationService.getReservationById(updated.getReservation().getReservationId());
                if (resOpt.isPresent()) {
                    table.setReservation(resOpt.get());
                } else {
                    return ResponseEntity.badRequest().body("Reservation not found");
                }
            } else {
                table.setReservation(null);
            }

            return ResponseEntity.ok(tableInfoService.saveTableInfo(table));
        }
        return ResponseEntity.notFound().build();
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTable(@PathVariable Integer id) {
        if (tableInfoService.getTableInfoById(id).isPresent()) {
            tableInfoService.deleteTableInfo(id);
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.status(404).body("Table not found");
    }


}
