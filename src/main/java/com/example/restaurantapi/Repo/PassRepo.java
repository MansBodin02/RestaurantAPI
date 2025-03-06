package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Pass.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
//Repo: PassRepo

import com.example.restaurantapi.Models.Pass.PassType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PassRepo extends JpaRepository<Pass, Long> {
    List<Pass> findByPassDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Pass> findByPassType(PassType passType);
}

