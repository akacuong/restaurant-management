package com.example.restaurantmanagement.infrastructure.dto;

public class MenuItemStats {
    private String name;
    private int totalSold;
    // constructor, getter, setter

    public MenuItemStats(String name, int totalSold) {
        this.name = name;
        this.totalSold = totalSold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }
}
