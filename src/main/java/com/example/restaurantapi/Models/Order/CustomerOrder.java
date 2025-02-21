package com.example.restaurantapi.Models.Order;

import com.example.restaurantapi.Models.Drink.Drink;
import com.example.restaurantapi.Models.Food.Food;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("orderId")
    private Long orderId;

    @Column(nullable = false)
    private int orderTable;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    // ManyToMany relation to Food
    @ManyToMany
    @JoinTable(
            name = "order_food",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")

    )
    private List<Food> foodOrders;

    @ManyToMany
    @JoinTable(
            name = "order_drink",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "drink_id")

    )
    private List<Drink> drinkOrders;



    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    @Column(nullable = false)
    private double orderPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState orderState;

    public CustomerOrder(Long orderId, int orderTable, LocalDateTime orderDateTime, List<Food> foodOrders, List<Drink> drinkOrders, List<OrderItem> orderItems, double orderPrice, OrderState orderState) {
        this.orderId = orderId;
        this.orderTable = orderTable;
        this.orderDateTime = orderDateTime;
        this.foodOrders = foodOrders;
        this.drinkOrders = drinkOrders;
        this.orderItems = orderItems;
        this.orderPrice = orderPrice;
        this.orderState = orderState;
    }

    public CustomerOrder() {

    }

    // Getters and Setters
    public int getOrderTable() { return orderTable; }
    public void setOrderTable(int orderTable) { this.orderTable = orderTable; }
    public LocalDateTime getOrderDateTime() { return orderDateTime; }
    public void setOrderDateTime(LocalDateTime orderDateTime) { this.orderDateTime = orderDateTime; }
    public List<Food> getFoodOrders() { return foodOrders; }
    public void setFoodOrders(List<Food> foodOrders) { this.foodOrders = foodOrders; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    public double getOrderPrice() { return orderPrice; }
    public void setOrderPrice(double orderPrice) { this.orderPrice = orderPrice; }
    public OrderState getOrderState() { return orderState; }
    public void setOrderState(OrderState orderState) { this.orderState = orderState; }

    public List<Drink> getDrinkOrders() {return drinkOrders;}

    public void setDrinkOrders(List<Drink> drinkOrders) {this.drinkOrders = drinkOrders;}
}
