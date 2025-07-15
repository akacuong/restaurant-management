package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Feedback;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FeedbackService {

    Feedback submitFeedback(Feedback feedback); // khách hàng gửi phản hồi

    Feedback approveFeedback(Feedback.FeedbackId id); // nhân viên duyệt

    void deleteFeedback(Feedback.FeedbackId id); // xóa phản hồi

    List<Feedback> getFeedbacksByMenuItem(Integer menuItemId); // lấy phản hồi cho 1 món

    List<Feedback> getPendingFeedbacks(); // phản hồi chưa duyệt

    List<Feedback> getComplaints(); // phản hồi tiêu cực

    Optional<Feedback> getFeedbackById(Feedback.FeedbackId id); // lấy 1 phản hồi cụ thể

    Double getAverageRating(Integer menuItemId); // trung bình đánh giá

    // ✅ Mới thêm để controller xử lý đơn giản
    Feedback buildFeedbackFromMap(Map<String, Object> body); // Tạo Feedback từ JSON/map

    Feedback.FeedbackId buildFeedbackIdFromMap(Map<String, Integer> body); // Tạo ID phản hồi từ JSON/map
}
