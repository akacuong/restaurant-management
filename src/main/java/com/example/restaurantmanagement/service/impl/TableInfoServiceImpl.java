package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.TableInfo;
import com.example.restaurantmanagement.repository.TableInfoRepository;
import com.example.restaurantmanagement.service.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableInfoServiceImpl implements TableInfoService {

    private final TableInfoRepository tableInfoRepository;

    @Autowired
    public TableInfoServiceImpl(TableInfoRepository tableInfoRepository) {
        this.tableInfoRepository = tableInfoRepository;
    }

    @Override
    public TableInfo saveTableInfo(TableInfo tableInfo) {
        return tableInfoRepository.save(tableInfo);
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
        tableInfoRepository.deleteById(id);
    }

}
