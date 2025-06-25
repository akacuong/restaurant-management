package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableInfoRepository extends JpaRepository<TableInfo,Integer> {
}
