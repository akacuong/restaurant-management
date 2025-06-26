package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TableInfoRepository extends JpaRepository<TableInfo,Integer> {
    @Query("SELECT t FROM TableInfo t WHERE t.status = 'EMPTY'")
    List<TableInfo> findAvailableTables();
    @Query("SELECT ti FROM TableInfo ti " +
            "WHERE ti.tableNumber = :tableNumber " +
            "AND ti.reservation.startTime < :endTime " +
            "AND ti.reservation.endTime > :startTime")
    List<TableInfo> findConflicts(@Param("tableNumber") String tableNumber,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);


}
