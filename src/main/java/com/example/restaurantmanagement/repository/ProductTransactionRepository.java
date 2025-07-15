package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.ProductTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductTransactionRepository extends JpaRepository<ProductTransaction, Integer> {

    List<ProductTransaction> findByProductId(Integer productId);
    @Query("SELECT pt FROM ProductTransaction pt WHERE pt.type = :type AND pt.transactionTime > :afterTime")
    List<ProductTransaction> findByTypeAndTransactionTimeAfter(
            @Param("type") ProductTransaction.Type type,
            @Param("afterTime") LocalDateTime afterTime
    );
}
