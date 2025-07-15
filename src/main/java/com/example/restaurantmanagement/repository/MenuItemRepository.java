package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    @Query("SELECT m FROM MenuItem m " +
            "LEFT JOIN m.category c " +
            "WHERE (:keyword IS NOT NULL AND (" +
            "LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))) " +
            "ORDER BY m.price ASC")
    List<MenuItem> searchByKeywordSorted(@Param("keyword") String keyword);
    List<MenuItem> findAllByOrderByPriceAsc();

}
