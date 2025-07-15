package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.Customer;
import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.model.Order;
import com.example.restaurantmanagement.model.OrderDetail;
import com.example.restaurantmanagement.repository.CustomerRepository;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.repository.OrderDetailRepository;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.service.MenuRecommendationService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuRecommendationServiceImpl implements MenuRecommendationService {

    private final OrderDetailRepository orderDetailRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public MenuRecommendationServiceImpl(OrderDetailRepository orderDetailRepository,
                                         MenuItemRepository menuItemRepository,
                                         OrderRepository orderRepository,
                                         CustomerRepository customerRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
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

    @Override
    public List<MenuItem> getPersonalizedRecommendations(int customerId, int limit) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return List.of(); // Không tìm thấy customer
        }

        Customer customer = customerOpt.get();
        List<Order> orders = orderRepository.findByCustomer(customer);

        if (orders.isEmpty()) {
            return List.of(); // Khách chưa có đơn hàng nào
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderIn(orders);

        Map<MenuItem, Integer> frequencyMap = new HashMap<>();
        for (OrderDetail detail : orderDetails) {
            MenuItem item = detail.getItem();
            int qty = detail.getQuantity();
            frequencyMap.put(item, frequencyMap.getOrDefault(item, 0) + qty);
        }

        return frequencyMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue()) // Desc theo số lần gọi
                .map(Map.Entry::getKey)
                .limit(limit)
                .toList();
    }
    @Override
    public List<MenuItem> getAssociatedItems(Integer baseItemId, int limit) {
        Optional<MenuItem> baseItemOpt = menuItemRepository.findById(baseItemId);
        if (baseItemOpt.isEmpty()) return List.of();

        MenuItem baseItem = baseItemOpt.get();
        List<Order> allOrders = orderRepository.findAll();

        Map<Set<Integer>, Integer> itemsetCount = new HashMap<>();
        Map<Integer, Integer> itemCount = new HashMap<>();

        for (Order order : allOrders) {
            List<OrderDetail> details = orderDetailRepository.findByOrder(order);
            Set<Integer> itemsInOrder = details.stream()
                    .map(d -> d.getItem().getId())
                    .collect(java.util.stream.Collectors.toSet());

            for (Integer item1 : itemsInOrder) {
                itemCount.put(item1, itemCount.getOrDefault(item1, 0) + 1);

                for (Integer item2 : itemsInOrder) {
                    if (!item1.equals(item2)) {
                        Set<Integer> pair = new HashSet<>(Set.of(item1, item2));
                        itemsetCount.put(pair, itemsetCount.getOrDefault(pair, 0) + 1);
                    }
                }
            }
        }

        Map<Integer, Integer> recommended = new HashMap<>();
        for (Map.Entry<Set<Integer>, Integer> entry : itemsetCount.entrySet()) {
            Set<Integer> pair = entry.getKey();
            if (pair.contains(baseItemId)) {
                for (Integer other : pair) {
                    if (!other.equals(baseItemId)) {
                        double confidence = (double) entry.getValue() / itemCount.get(baseItemId);
                        if (confidence >= 0.5) { // threshold confidence
                            recommended.put(other, entry.getValue());
                        }
                    }
                }
            }
        }

        return recommended.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .map(entry -> menuItemRepository.findById(entry.getKey()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .limit(limit)
                .toList();
    }

}
