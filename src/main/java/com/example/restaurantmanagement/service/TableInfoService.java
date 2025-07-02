package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.TableInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableInfoService {

    TableInfo createTableInfo(TableInfo tableInfo);

    TableInfo updateTableInfo(TableInfo tableInfo);

    List<TableInfo> getAllTableInfos();

    Optional<TableInfo> getTableInfoById(Integer id);

    void deleteTableInfo(Integer id);

    List<TableInfo> getAvailableTables();
    List<List<TableInfo>> suggestTablesForReservation(int numberOfPeople, LocalDateTime startTime, LocalDateTime endTime);
}
