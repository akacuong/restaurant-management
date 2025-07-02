package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.repository.OrderDetailRepository;
import com.example.restaurantmanagement.service.MenuRecommendationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuRecommendationServiceImpl implements MenuRecommendationService {

    private final OrderDetailRepository orderDetailRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuRecommendationServiceImpl(OrderDetailRepository orderDetailRepository,
                                     MenuItemRepository menuItemRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<MenuItem> getPopularMenuItems(int limit) {
        List<Object[]> rawResults = orderDetailRepository.findPopularMenuItems();
        List<MenuItem> result = new ArrayList<>();

        for (int i = 0; i < Math.min(limit, rawResults.size()); i++) {
            Integer itemId = (Integer) rawResults.get(i)[0];
            menuItemRepository.findById(itemId).ifPresent(result::add);
        }

        return result;
    }
}
