package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Lunch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LunchRepo extends JpaRepository<Lunch, Long> {
    List<Lunch> findByLunchWeek(int lunchWeek);
    Optional<Lunch> findByLunchWeekAndLunchDay(int lunchWeek, int lunchDay);
}
