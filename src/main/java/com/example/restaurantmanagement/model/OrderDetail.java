package com.example.restaurantmanagement.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@IdClass(OrderDetail.OrderDetailId.class)
public class OrderDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private MenuItem item;

    private Integer quantity;


    // Composite key class
    public static class OrderDetailId implements Serializable {
        private Integer order;
        private Integer item;

        public OrderDetailId() {}

        public OrderDetailId(Integer order, Integer item) {
            this.order = order;
            this.item = item;
        }

        // equals() & hashCode()
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OrderDetailId that)) return false;
            return order.equals(that.order) && item.equals(that.item);
        }

        @Override
        public int hashCode() {
            return order.hashCode() + item.hashCode();
        }
    }

    public OrderDetail() {}

    // Getters & Setters

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
