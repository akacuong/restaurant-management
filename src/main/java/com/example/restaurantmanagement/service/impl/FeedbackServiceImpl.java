package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.infrastructure.exception.ErrorCode;
import com.example.restaurantmanagement.infrastructure.exception.NVException;
import com.example.restaurantmanagement.model.*;
import com.example.restaurantmanagement.repository.FeedbackRepository;
import com.example.restaurantmanagement.repository.CustomerRepository;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuItemRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               CustomerRepository customerRepository,
                               MenuItemRepository menuItemRepository) {
        this.feedbackRepository = feedbackRepository;
        this.customerRepository = customerRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public Feedback submitFeedback(Feedback feedback) {
        if (feedback.getRating() < 1 || feedback.getRating() > 5) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Rating must be between 1 and 5"});
        }

        Feedback.FeedbackId id = new Feedback.FeedbackId(
                feedback.getCustomer().getId(),
                feedback.getMenuItem().getId()
        );

        if (feedbackRepository.existsById(id)) {
            throw new NVException(ErrorCode.DUPLICATE_RESOURCE, new Object[]{"Feedback already exists"});
        }

        feedback.setApproved(false);
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback approveFeedback(Feedback.FeedbackId id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new NVException(ErrorCode.RESOURCE_NOT_FOUND, new Object[]{"Feedback not found"}));
        feedback.setApproved(true);
        return feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(Feedback.FeedbackId id) {
        if (!feedbackRepository.existsById(id)) {
            throw new NVException(ErrorCode.RESOURCE_NOT_FOUND, new Object[]{"Feedback not found"});
        }
        feedbackRepository.deleteById(id);
    }

    @Override
    public List<Feedback> getFeedbacksByMenuItem(Integer menuItemId) {
        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NVException(ErrorCode.RESOURCE_NOT_FOUND, new Object[]{"Menu item not found"}));
        return feedbackRepository.findByMenuItem(item);
    }

    @Override
    public List<Feedback> getPendingFeedbacks() {
        return feedbackRepository.findByApprovedFalse();
    }

    @Override
    public List<Feedback> getComplaints() {
        return feedbackRepository.findByRatingLessThanEqualAndApprovedTrue(2);
    }

    @Override
    public Optional<Feedback> getFeedbackById(Feedback.FeedbackId id) {
        return feedbackRepository.findById(id);
    }

    @Override
    public Double getAverageRating(Integer menuItemId) {
        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NVException(ErrorCode.RESOURCE_NOT_FOUND, new Object[]{"Menu item not found"}));
        return feedbackRepository.findAverageRatingByMenuItem(item);
    }

    //  Xây dựng Feedback từ dữ liệu đầu vào của client
    @Override
    public Feedback buildFeedbackFromMap(Map<String, Object> body) {
        Integer customerId = (Integer) body.get("customerId");
        Integer menuItemId = (Integer) body.get("menuItemId");
        Integer rating = (Integer) body.get("rating");
        String comment = (String) body.get("comment");

        if (customerId == null || menuItemId == null || rating == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing required fields"});
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NVException(ErrorCode.RESOURCE_NOT_FOUND, new Object[]{"Customer not found"}));
        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NVException(ErrorCode.RESOURCE_NOT_FOUND, new Object[]{"Menu item not found"}));

        Feedback feedback = new Feedback();
        feedback.setCustomer(customer);
        feedback.setMenuItem(item);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setApproved(false);

        return feedback;
    }

    //  Tạo FeedbackId từ Map (ví dụ để approve hoặc delete)
    @Override
    public Feedback.FeedbackId buildFeedbackIdFromMap(Map<String, Integer> body) {
        Integer customerId = body.get("customerId");
        Integer menuItemId = body.get("menuItemId");

        if (customerId == null || menuItemId == null) {
            throw new NVException(ErrorCode.INVALID_REQUEST, new Object[]{"Missing customerId or menuItemId"});
        }

        return new Feedback.FeedbackId(customerId, menuItemId);
    }
}
