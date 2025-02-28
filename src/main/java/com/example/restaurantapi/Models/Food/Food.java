package com.example.restaurantapi.Models.Food;

import com.example.restaurantapi.Models.Order.CustomerOrder;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"food_name"})
)
public class Food {
    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    @Column(nullable = false)
    private String foodName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodCategory foodCategory;

    @Column
    private String foodDescription;

    @Column(nullable = false)
    private double foodPrice;

    // ManyToMany relation to CustomerOrder
    @ManyToMany(mappedBy = "foodOrders")
    private List<CustomerOrder> customerOrders;

    public Food(Long foodId, String foodName, FoodType foodType, FoodCategory foodCategory, String foodDescription, double foodPrice, List<CustomerOrder> customerOrders) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodCategory = foodCategory;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.customerOrders = customerOrders;
    }

    public Food() {

    }

    // Getters and Setters
    public Long getFoodId() { return foodId; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public FoodType getFoodType() { return foodType; }
    public void setFoodType(FoodType foodType) { this.foodType = foodType; }
    public FoodCategory getFoodCategory() { return foodCategory; }
    public void setFoodCategory(FoodCategory foodCategory) { this.foodCategory = foodCategory; }
    public String getFoodDescription() { return foodDescription; }
    public void setFoodDescription(String foodDescription) { this.foodDescription = foodDescription; }
    public double getFoodPrice() { return foodPrice; }
    public void setFoodPrice(double foodPrice) { this.foodPrice = foodPrice; }
    public List<CustomerOrder> getCustomerOrders() { return customerOrders; }
    public void setCustomerOrders(List<CustomerOrder> customerOrders) { this.customerOrders = customerOrders; }
}
