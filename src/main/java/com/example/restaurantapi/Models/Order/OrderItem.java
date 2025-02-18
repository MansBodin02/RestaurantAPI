package com.example.restaurantapi.Models.Order;

import com.example.restaurantapi.Models.Drink.Drink;
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
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;




    @Column
    private int foodQuantity;

    @Column
    private int drinkQuantity;

    @Column
    private String orderItemSpecial;

    public OrderItem(CustomerOrder customerOrder, Food food, int foodQuantity, Drink drink, int drinkQuantity, String orderItemSpecial) {
        this.customerOrder = customerOrder;
        this.food = food;
        this.drink = drink;
        this.foodQuantity = foodQuantity;
        this.drinkQuantity = drinkQuantity;
        this.orderItemSpecial = orderItemSpecial;
    }

    public OrderItem() {

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

    public Drink getDrink() {return drink;}

    public void setDrink(Drink drink) {this.drink = drink;}

    public int getDrinkQuantity() {return drinkQuantity;}

    public void setDrinkQuantity(int drinkQuantity) {this.drinkQuantity = drinkQuantity;}
}
