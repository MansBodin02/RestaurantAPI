package com.example.restaurantapi.Models;

import jakarta.persistence.*;

/**
 * ingen avancerad modell
 *
 */
@Entity
public class Lunch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String lunchName;
    @Column
    private int lunchWeek;
    @Column
    private int lunchDay;
    @Column
    private double price;
    @Column
    private String description;

    // ignorera atm
    public Long getId() {
        return id;
    }
    // ignorera atm
    public void setId(Long id) {
        this.id = id;
    }

    public String getLunchName() {
        return lunchName;
    }

    public void setLunchName(String lunchName) {
        this.lunchName = lunchName;
    }

    public int getLunchWeek() {
        return lunchWeek;
    }

    public void setLunchWeek(int lunchWeek) {
        this.lunchWeek = lunchWeek;
    }

    public int getLunchDay() {
        return lunchDay;
    }

    public void setLunchDay(int lunchDay) {
        this.lunchDay = lunchDay;
    }

    public double getPrice() { // Getter för price
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
