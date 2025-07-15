package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Feedback;
import com.example.restaurantmanagement.model.Feedback.FeedbackId;
import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, FeedbackId> {

    // Tìm phản hồi của 1 khách hàng về 1 món ăn
    Optional<Feedback> findByCustomerAndMenuItem(Customer customer, MenuItem menuItem);

    // Lấy tất cả phản hồi cho 1 món ăn
    List<Feedback> findByMenuItem(MenuItem menuItem);

    // Lấy tất cả phản hồi chưa duyệt
    List<Feedback> findByApprovedFalse();

    // Lấy phản hồi đã duyệt
    List<Feedback> findByApprovedTrue();

    // Thống kê trung bình đánh giá theo món ăn
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.menuItem = :menuItem AND f.approved = true")
    Double findAverageRatingByMenuItem(MenuItem menuItem);

    // Tìm các phản hồi có rating thấp (phàn nàn)
    List<Feedback> findByRatingLessThanEqualAndApprovedTrue(int threshold); // e.g. threshold = 2
}
