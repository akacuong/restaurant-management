package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.OrderDetail;
import com.example.restaurantmanagement.model.OrderDetail.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

    @Query("SELECT od FROM OrderDetail od WHERE od.order.id = :orderId")
    List<OrderDetail> findByOrderOrderId(@Param("orderId") Integer orderId);
    @Query("SELECT od.item.id, COUNT(od) as total " +
            "FROM OrderDetail od GROUP BY od.item.id ORDER BY total DESC")
    List<Object[]> findPopularMenuItems();
}
