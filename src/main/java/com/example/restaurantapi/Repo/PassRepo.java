package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Pass.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
//Repo: PassRepo

public interface PassRepo extends JpaRepository<Pass, Long> {
}
