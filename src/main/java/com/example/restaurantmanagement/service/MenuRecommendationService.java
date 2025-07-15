package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.MenuItem;
import java.util.List;

public interface MenuRecommendationService {
    List<MenuItem> getPopularMenuItems(int limit);
    List<MenuItem> getPersonalizedRecommendations(int customerId, int limit);
    List<MenuItem> getAssociatedItems(Integer baseItemId, int limit);

}