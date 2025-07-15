package com.example.restaurantmanagement.controllers;

import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.response.ResponseObject;
import com.example.restaurantmanagement.service.MenuRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
public class MenuRecommendationController {

    private final MenuRecommendationService recommendationService;

    public MenuRecommendationController(MenuRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    //Món phổ biến toàn hệ thống
    @GetMapping("/popular")
    public ResponseEntity<ResponseObject> getPopularMenuItems(@RequestParam(defaultValue = "5") int limit) {
        List<MenuItem> popularItems = recommendationService.getPopularMenuItems(limit);
        return ResponseEntity.ok(new ResponseObject(popularItems));
    }
    //Gợi ý món cá nhân hóa theo lịch sử đặt món
    @GetMapping("/personalized")
    public ResponseEntity<ResponseObject> getPersonalizedMenuItems(
            @RequestParam int customerId,
            @RequestParam(defaultValue = "3") int limit) {
        List<MenuItem> personalizedItems = recommendationService.getPersonalizedRecommendations(customerId, limit);
        return ResponseEntity.ok(new ResponseObject(personalizedItems));
    }
    // Gợi ý món đi kèm (combo)
    @GetMapping("/associated")
    public ResponseEntity<ResponseObject> getAssociatedItems(
            @RequestParam int itemId,
            @RequestParam(defaultValue = "3") int limit) {
        List<MenuItem> associatedItems = recommendationService.getAssociatedItems(itemId, limit);
        return ResponseEntity.ok(new ResponseObject(associatedItems));
    }
}
