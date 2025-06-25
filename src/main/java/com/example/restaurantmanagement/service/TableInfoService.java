package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.TableInfo;

import java.util.List;
import java.util.Optional;

public interface TableInfoService {

    TableInfo saveTableInfo(TableInfo tableInfo);

    Optional<TableInfo> getTableInfoById(Integer id);

    List<TableInfo> getAllTableInfos();

    void deleteTableInfo(Integer id);

}
