package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.Reservation;
import com.example.restaurantmanagement.model.TableInfo;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.ReservationService;
import com.example.restaurantmanagement.service.TableInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
public class TableInfoController {

    private final TableInfoService tableInfoService;
    private final ReservationService reservationService;

    public TableInfoController(TableInfoService tableInfoService, ReservationService reservationService) {
        this.tableInfoService = tableInfoService;
        this.reservationService = reservationService;
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createTable(@RequestBody TableInfo tableInfo) {
        if (tableInfo.getReservation() != null && tableInfo.getReservation().getId() != null) {
            Integer resId = tableInfo.getReservation().getId();
            Reservation reservation = reservationService.getReservationById(resId)
                    .orElseThrow(() -> new NVException(ErrorCode.RESERVATION_NOT_FOUND));
            tableInfo.setReservation(reservation);
        } else {
            tableInfo.setReservation(null);
        }

        TableInfo created = tableInfoService.createTableInfo(tableInfo);
        return ResponseEntity.ok(new ResponseObject(created));
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllTables() {
        return ResponseEntity.ok(new ResponseObject(tableInfoService.getAllTableInfos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getTable(@PathVariable Integer id) {
        TableInfo table = tableInfoService.getTableInfoById(id)
                .orElseThrow(() -> new NVException(ErrorCode.TABLE_NOT_FOUND));
        return ResponseEntity.ok(new ResponseObject(table));
    }
    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateTable(@RequestBody Integer id,
                                                      @RequestBody TableInfo updated) {
        TableInfo existing = tableInfoService.getTableInfoById(id)
                .orElseThrow(() -> new NVException(ErrorCode.TABLE_NOT_FOUND));

        existing.setTableNumber(updated.getTableNumber());
        existing.setCapacity(updated.getCapacity());
        existing.setStatus(updated.getStatus());

        if (updated.getReservation() != null && updated.getReservation().getId() != null) {
            Reservation reservation = reservationService.getReservationById(updated.getReservation().getId())
                    .orElseThrow(() -> new NVException(ErrorCode.RESERVATION_NOT_FOUND));
            existing.setReservation(reservation);
        } else {
            existing.setReservation(null);
        }
        TableInfo updatedTable = tableInfoService.updateTableInfo(existing);
        return ResponseEntity.ok(new ResponseObject(updatedTable));
    }
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteTable(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        if (id == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST);
        }

        TableInfo table = tableInfoService.getTableInfoById(id)
                .orElseThrow(() -> new NVException(ErrorCode.TABLE_NOT_FOUND));

        tableInfoService.deleteTableInfo(table.getId());
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Table deleted successfully"));
    }
    @GetMapping("/available")
    public ResponseEntity<ResponseObject> getAvailableTables() {
        List<TableInfo> availableTables = tableInfoService.getAvailableTables();
        return ResponseEntity.ok(new ResponseObject(availableTables));
    }
    @GetMapping("/suggest")
    public ResponseEntity<ResponseObject> suggestTableCombinations(@RequestParam int numberOfPeople,
                                                                   @RequestParam String start,
                                                                   @RequestParam String end) {
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);

        List<List<TableInfo>> suggestions = tableInfoService.suggestTablesForReservation(numberOfPeople, startTime, endTime);
        return ResponseEntity.ok(new ResponseObject(suggestions));
    }
}
