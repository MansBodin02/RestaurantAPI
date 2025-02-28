package com.example.restaurantapi.Models.Drink;

import com.example.restaurantapi.Models.Order.CustomerOrder;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"drink_name"})
)
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drinkId;

    @Column(nullable = false)
    private String drinkName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrinkCategory drinkCategory;

    @Column
    private String drinkDescription;

    @Column(nullable = false)
    private double drinkPrice;

    // ManyToMany relation till CustomerOrder
    @ManyToMany(mappedBy = "drinkOrders")
    private List<CustomerOrder> customerOrders;

    public Drink(Long drinkId, String drinkName, DrinkCategory drinkCategory, String drinkDescription, double drinkPrice, List<CustomerOrder> customerOrders) {
        this.drinkId = drinkId;
        this.drinkName = drinkName;
        this.drinkCategory = drinkCategory;
        this.drinkDescription = drinkDescription;
        this.drinkPrice = drinkPrice;
        this.customerOrders = customerOrders;
    }

    public Drink() {

    }

    // Getters och Setters
    public Long getDrinkId() { return drinkId; }
    public void setDrinkId(Long drinkId) { this.drinkId = drinkId; }

    public String getDrinkName() { return drinkName; }
    public void setDrinkName(String drinkName) { this.drinkName = drinkName; }

    public DrinkCategory getDrinkCategory() { return drinkCategory; }
    public void setDrinkCategory(DrinkCategory drinkCategory) { this.drinkCategory = drinkCategory; }

    public String getDrinkDescription() { return drinkDescription; }
    public void setDrinkDescription(String drinkDescription) { this.drinkDescription = drinkDescription; }

    public double getDrinkPrice() { return drinkPrice; }
    public void setDrinkPrice(double drinkPrice) { this.drinkPrice = drinkPrice; }

    public List<CustomerOrder> getCustomerOrders() { return customerOrders; }
    public void setCustomerOrders(List<CustomerOrder> customerOrders) { this.customerOrders = customerOrders; }
}
