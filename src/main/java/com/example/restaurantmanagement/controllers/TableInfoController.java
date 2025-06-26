package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Reservation;
import com.example.restaurantmanagement.model.TableInfo;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.ReservationService;
import com.example.restaurantmanagement.service.TableInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
public class TableInfoController {

    private final TableInfoService tableInfoService;
    private final ReservationService reservationService;

    public TableInfoController(TableInfoService tableInfoService, ReservationService reservationService) {
        this.tableInfoService = tableInfoService;
        this.reservationService = reservationService;
    }

    // ✅ CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createTable(@RequestBody TableInfo tableInfo) {
        try {
            if (tableInfo.getReservation() != null && tableInfo.getReservation().getId() != null) {
                Integer resId = tableInfo.getReservation().getId();
                Optional<Reservation> resOpt = reservationService.getReservationById(resId);
                if (resOpt.isPresent()) {
                    tableInfo.setReservation(resOpt.get());
                } else {
                    return ResponseEntity.badRequest()
                            .body(new ResponseObject("CREATE_FAILED", "Reservation with ID " + resId + " not found"));
                }
            } else {
                tableInfo.setReservation(null);
            }

            TableInfo created = tableInfoService.createTableInfo(tableInfo);
            return ResponseEntity.ok(new ResponseObject(created));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("CREATE_FAILED", e.getMessage()));
        }
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<ResponseObject> getAllTables() {
        List<TableInfo> tables = tableInfoService.getAllTableInfos();
        return ResponseEntity.ok(new ResponseObject(tables));
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getTable(@PathVariable Integer id) {
        Optional<TableInfo> table = tableInfoService.getTableInfoById(id);
        return table.map(value -> ResponseEntity.ok(new ResponseObject(value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("NOT_FOUND", "Table not found")));
    }

    // ✅ UPDATE
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateTable(@PathVariable Integer id,
                                                      @RequestBody TableInfo updated) {
        Optional<TableInfo> tableOpt = tableInfoService.getTableInfoById(id);
        if (tableOpt.isPresent()) {
            TableInfo existing = tableOpt.get();
            existing.setTableNumber(updated.getTableNumber());
            existing.setCapacity(updated.getCapacity());
            existing.setStatus(updated.getStatus());

            if (updated.getReservation() != null && updated.getReservation().getId() != null) {
                Optional<Reservation> resOpt = reservationService.getReservationById(updated.getReservation().getId());
                if (resOpt.isPresent()) {
                    existing.setReservation(resOpt.get());
                } else {
                    return ResponseEntity.badRequest()
                            .body(new ResponseObject("UPDATE_FAILED", "Reservation not found"));
                }
            } else {
                existing.setReservation(null);
            }

            TableInfo updatedTable = tableInfoService.updateTableInfo(existing);
            return ResponseEntity.ok(new ResponseObject(updatedTable));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("NOT_FOUND", "Table not found with ID = " + id));
    }

    // ✅ DELETE
    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteTable(@PathVariable Integer id) {
        Optional<TableInfo> tableOpt = tableInfoService.getTableInfoById(id);
        if (tableOpt.isPresent()) {
            tableInfoService.deleteTableInfo(id);
            return ResponseEntity.ok(new ResponseObject("SUCCESS", "Table deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("NOT_FOUND", "Table not found with ID = " + id));
    }
    // ✅ Lấy bàn trống
    @GetMapping("/available")
    public ResponseEntity<ResponseObject> getAvailableTables() {
        List<TableInfo> availableTables = tableInfoService.getAvailableTables();
        return ResponseEntity.ok(new ResponseObject(availableTables));
    }


}
