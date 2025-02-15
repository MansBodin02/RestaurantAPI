package com.example.restaurantapi.Models.Order;

import com.example.restaurantapi.Models.Food.Food;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private int orderTable;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    // @ManyToMany relation to Food
    @OneToMany
    @JoinTable(
            name = "order_food",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foodOrders;

    @Column
    private String orderSpecial;

    @Column(nullable = false)
    private double orderPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState orderState;


    // Getters and Setters
    public int getOrderTable() { return orderTable; }
    public void setOrderTable(int orderTable) { this.orderTable = orderTable; }
    public LocalDateTime getOrderDateTime() { return orderDateTime; }
    public void setOrderDateTime(LocalDateTime orderDateTime) { this.orderDateTime = orderDateTime; }
    public List<Food> getFoodOrders() { return foodOrders; }
    public void setFoodOrders(List<Food> foodOrders) { this.foodOrders = foodOrders; }
    public String getOrderSpecial() { return orderSpecial; }
    public void setOrderSpecial(String orderSpecial) { this.orderSpecial = orderSpecial; }
    public double getOrderPrice() { return orderPrice; }
    public void setOrderPrice(double orderPrice) { this.orderPrice = orderPrice; }
    public OrderState getOrderState() { return orderState; }
    public void setOrderState(OrderState orderState) { this.orderState = orderState; }
}


