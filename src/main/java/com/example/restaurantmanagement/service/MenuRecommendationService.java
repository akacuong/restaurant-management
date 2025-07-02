package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.MenuItem;
import java.util.List;

public interface MenuRecommendationService {
    List<MenuItem> getPopularMenuItems(int limit);
}