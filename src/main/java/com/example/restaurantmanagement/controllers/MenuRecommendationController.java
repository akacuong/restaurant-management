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

    @GetMapping("/popular")
    public ResponseEntity<ResponseObject> getPopularMenuItems(@RequestParam(defaultValue = "5") int limit) {
        List<MenuItem> popularItems = recommendationService.getPopularMenuItems(limit);
        return ResponseEntity.ok(new ResponseObject(popularItems));
    }
}
