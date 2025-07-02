package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.TableInfo;
import com.example.restaurantmanagement.repository.TableInfoRepository;
import com.example.restaurantmanagement.service.TableInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TableInfoServiceImpl implements TableInfoService {

    private final TableInfoRepository tableInfoRepository;

    public TableInfoServiceImpl(TableInfoRepository tableInfoRepository) {
        this.tableInfoRepository = tableInfoRepository;
    }

    @Override
    public TableInfo createTableInfo(TableInfo tableInfo) {
        if (tableInfo.getId() != null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"New table must not have an ID"});
        }
        return tableInfoRepository.save(tableInfo);
    }

    @Override
    public TableInfo updateTableInfo(TableInfo updatedTable) {
        if (updatedTable.getId() == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Table ID must not be null"});
        }

        Optional<TableInfo> existingOpt = tableInfoRepository.findById(updatedTable.getId());
        if (existingOpt.isEmpty()) {
            throw new NVException(ErrorCode.TABLE_NOT_FOUND, new Object[]{"ID = " + updatedTable.getId()});
        }

        TableInfo existing = existingOpt.get();
        existing.setTableNumber(updatedTable.getTableNumber());
        existing.setCapacity(updatedTable.getCapacity());
        existing.setStatus(updatedTable.getStatus());
        existing.setReservation(updatedTable.getReservation());

        return tableInfoRepository.save(existing);
    }

    @Override
    public Optional<TableInfo> getTableInfoById(Integer id) {
        return tableInfoRepository.findById(id);
    }

    @Override
    public List<TableInfo> getAllTableInfos() {
        return tableInfoRepository.findAll();
    }

    @Override
    public void deleteTableInfo(Integer id) {
        if (!tableInfoRepository.existsById(id)) {
            throw new NVException(ErrorCode.TABLE_NOT_FOUND, new Object[]{"ID = " + id});
        }
        tableInfoRepository.deleteById(id);
    }

    @Override
    public List<TableInfo> getAvailableTables() {
        return tableInfoRepository.findAvailableTables();
    }
    //Gợi ý bàn ưu tiên'
    @Override
    public List<List<TableInfo>> suggestTablesForReservation(int numberOfPeople, LocalDateTime startTime, LocalDateTime endTime) {
        List<TableInfo> availableTables = tableInfoRepository.findAvailableTablesDuring(startTime, endTime);
        List<List<TableInfo>> bestCombinations = new ArrayList<>();
        int minOverCapacity = Integer.MAX_VALUE;
        int minTableCount = Integer.MAX_VALUE;
        int n = availableTables.size();
        for (int mask = 1; mask < (1 << n); mask++) {
            List<TableInfo> currentCombo = new ArrayList<>();
            int total = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    TableInfo t = availableTables.get(i);
                    currentCombo.add(t);
                    total += t.getCapacity();
                }
            }
            if (total >= numberOfPeople) {
                int over = total - numberOfPeople;
                if (over < minOverCapacity || (over == minOverCapacity && currentCombo.size() < minTableCount)) {
                    bestCombinations.clear();
                    bestCombinations.add(currentCombo);
                    minOverCapacity = over;
                    minTableCount = currentCombo.size();
                } else if (over == minOverCapacity && currentCombo.size() == minTableCount) {
                    bestCombinations.add(currentCombo);
                }
            }
        }
        if (bestCombinations.isEmpty()) {
            throw new NVException(ErrorCode.TIME_INVALID, new Object[]{"Không đủ bàn phù hợp cho số người yêu cầu"});
        }
        return bestCombinations;
    }
}
