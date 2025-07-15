package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.Feedback;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ResponseObject> submitFeedback(@RequestBody Map<String, Object> body) {
        Feedback feedback = feedbackService.buildFeedbackFromMap(body);
        Feedback saved = feedbackService.submitFeedback(feedback);
        return ResponseEntity.ok(new ResponseObject(saved));
    }


    @PostMapping("/approve")
    public ResponseEntity<ResponseObject> approveFeedback(@RequestBody Map<String, Integer> body) {
        Feedback.FeedbackId id = feedbackService.buildFeedbackIdFromMap(body);
        Feedback approved = feedbackService.approveFeedback(id);
        return ResponseEntity.ok(new ResponseObject(approved));
    }


    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteFeedback(@RequestBody Map<String, Integer> body) {
        Feedback.FeedbackId id = feedbackService.buildFeedbackIdFromMap(body);
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "Feedback deleted successfully"));
    }


    @GetMapping("/menu-item/{menuItemId}")
    public ResponseEntity<ResponseObject> getFeedbacksByMenuItem(@PathVariable Integer menuItemId) {
        List<Feedback> list = feedbackService.getFeedbacksByMenuItem(menuItemId);
        return ResponseEntity.ok(new ResponseObject(list));
    }


    @GetMapping("/pending")
    public ResponseEntity<ResponseObject> getPendingFeedbacks() {
        List<Feedback> list = feedbackService.getPendingFeedbacks();
        return ResponseEntity.ok(new ResponseObject(list));
    }

    @GetMapping("/complaints")
    public ResponseEntity<ResponseObject> getComplaints() {
        List<Feedback> list = feedbackService.getComplaints();
        return ResponseEntity.ok(new ResponseObject(list));
    }

    @GetMapping("/average-rating/{menuItemId}")
    public ResponseEntity<ResponseObject> getAverageRating(@PathVariable Integer menuItemId) {
        Double average = feedbackService.getAverageRating(menuItemId);
        return ResponseEntity.ok(new ResponseObject(average));
    }
}
