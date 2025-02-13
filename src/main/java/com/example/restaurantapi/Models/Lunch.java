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
}
