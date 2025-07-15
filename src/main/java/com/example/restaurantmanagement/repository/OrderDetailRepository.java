package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Order;
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
    //Lấy danh sách món phổ biến (dựa trên số lần xuất hiện)
    List<Object[]> findPopularMenuItems();
    //Lấy chi tiết đơn hàng theo orderId
    List<OrderDetail> findByOrderIn(List<Order> orders);

    List<OrderDetail> findByOrder(Order order);
}
