package com.example.restaurantapi.Models.Order;

import com.example.restaurantapi.Models.Food.Food;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private CustomerOrder customerOrder;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column
    private int foodQuantity;
    @Column
    private String orderItemSpecial;

    public OrderItem() {}

    public OrderItem(CustomerOrder customerOrder, Food food, int foodQuantity, String orderItemSpecial) {
        this.customerOrder = customerOrder;
        this.food = food;
        this.foodQuantity = foodQuantity;
        this.orderItemSpecial = orderItemSpecial;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getOrderItemSpecial() {
        return orderItemSpecial;
    }

    public void setOrderItemSpecial(String orderItemSpecial) {
        this.orderItemSpecial = orderItemSpecial;
    }
}
