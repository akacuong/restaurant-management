package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.TableInfo;
import com.example.restaurantmanagement.repository.TableInfoRepository;
import com.example.restaurantmanagement.service.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TableInfoServiceImpl implements TableInfoService {

    private final TableInfoRepository tableInfoRepository;

    public TableInfoServiceImpl(TableInfoRepository tableInfoRepository) {
        this.tableInfoRepository = tableInfoRepository;
    }

    // ✅ CREATE
    @Override
    public TableInfo createTableInfo(TableInfo tableInfo) {
        if (tableInfo.getId() != null) {
            throw new IllegalArgumentException("New table must not have an ID");
        }
        return tableInfoRepository.save(tableInfo);
    }

    // ✅ UPDATE
    @Override
    public TableInfo updateTableInfo(TableInfo updatedTable) {
        if (updatedTable.getId() == null) {
            throw new IllegalArgumentException("Table ID must not be null");
        }

        Optional<TableInfo> existingOpt = tableInfoRepository.findById(updatedTable.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Table not found with ID = " + updatedTable.getId());
        }

        TableInfo existing = existingOpt.get();
        existing.setTableNumber(updatedTable.getTableNumber());
        existing.setCapacity(updatedTable.getCapacity());
        existing.setStatus(updatedTable.getStatus());
        existing.setReservation(updatedTable.getReservation());

        return tableInfoRepository.save(existing);
    }

    // ✅ READ BY ID
    @Override
    public Optional<TableInfo> getTableInfoById(Integer id) {
        return tableInfoRepository.findById(id);
    }

    // ✅ READ ALL
    @Override
    public List<TableInfo> getAllTableInfos() {
        return tableInfoRepository.findAll();
    }

    // ✅ DELETE
    @Override
    public void deleteTableInfo(Integer id) {
        if (!tableInfoRepository.existsById(id)) {
            throw new RuntimeException("Table not found with ID = " + id);
        }
        tableInfoRepository.deleteById(id);
    }
    //find empty table
    @Override
    public List<TableInfo> getAvailableTables() {
        return tableInfoRepository.findAvailableTables();
    }


}
