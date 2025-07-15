package com.example.restaurantmanagement.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@IdClass(Feedback.FeedbackId.class)
public class Feedback {
    @Id
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Id
    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    private int rating; // 1–5 sao

    @Column(length = 1000)
    private String comment;

    private boolean approved = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Feedback() {}

    public Feedback(Customer customer, MenuItem menuItem, int rating, String comment) {
        this.customer = customer;
        this.menuItem = menuItem;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
        this.approved = false;
    }

    // Composite Key Class
    public static class FeedbackId implements Serializable {
        private Integer customer;
        private Integer menuItem;

        public FeedbackId() {}

        public FeedbackId(Integer customer, Integer menuItem) {
            this.customer = customer;
            this.menuItem = menuItem;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof FeedbackId that)) return false;
            return customer.equals(that.customer) && menuItem.equals(that.menuItem);
        }

        @Override
        public int hashCode() {
            return customer.hashCode() + menuItem.hashCode();
        }
    }

    // Getters & Setters

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
