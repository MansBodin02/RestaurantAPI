package com.example.restaurantapi.Models.Lunch;

import jakarta.persistence.*;

/**
 * Ingen avancerad modell för Lunch
 */
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"lunch_week", "lunch_day"})
)
public class Lunch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lunchId;

    @Column(nullable = false)
    private String lunchName;

    @Column(nullable = false)
    private int lunchWeek;

    @Column(nullable = false)
    private int lunchDay;

    @Column(nullable = false)
    private double lunchPrice;

    @Column(nullable = false)
    private String lunchDescription;

    public Lunch(Long lunchId, String lunchName, int lunchWeek, int lunchDay, double lunchPrice, String lunchDescription) {
        this.lunchId = lunchId;
        this.lunchName = lunchName;
        this.lunchWeek = lunchWeek;
        this.lunchDay = lunchDay;
        this.lunchPrice = lunchPrice;
        this.lunchDescription = lunchDescription;
    }

    public Lunch() {

    }


    // Setters och Getters
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

    public double getLunchPrice() {
        return lunchPrice;
    }

    public void setLunchPrice(double lunchPrice) {
        this.lunchPrice = lunchPrice;
    }

    public String getLunchDescription() {
        return lunchDescription;
    }

    public void setLunchDescription(String lunchDescription) {
        this.lunchDescription = lunchDescription;
    }
}
