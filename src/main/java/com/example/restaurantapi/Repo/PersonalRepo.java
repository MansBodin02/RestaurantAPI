package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Personal.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//Repo: PersonalRepo

@Repository
public interface PersonalRepo extends JpaRepository<Personal, Long> {
}