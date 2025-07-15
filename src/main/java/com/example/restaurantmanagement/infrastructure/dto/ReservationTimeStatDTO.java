package com.example.restaurantmanagement.infrastructure.dto;
public class ReservationTimeStatDTO {
    private String dayOfWeek;
    private int hour;
    private long count;

    public ReservationTimeStatDTO(String dayOfWeek, int hour, long count) {
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.count = count;
    }
    // Getters and setters

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}

