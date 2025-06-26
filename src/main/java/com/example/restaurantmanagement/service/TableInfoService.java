package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.TableInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableInfoService {

    TableInfo createTableInfo(TableInfo tableInfo);           // ✅ CREATE

    TableInfo updateTableInfo(TableInfo tableInfo);           // ✅ UPDATE

    List<TableInfo> getAllTableInfos();                       // ✅ READ ALL

    Optional<TableInfo> getTableInfoById(Integer id);         // ✅ READ ONE

    void deleteTableInfo(Integer id);                         // ✅ DELETE

    List<TableInfo> getAvailableTables();

}
