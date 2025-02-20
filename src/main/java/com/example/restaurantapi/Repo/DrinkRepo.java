package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Drink.Drink;
import com.example.restaurantapi.Models.Drink.DrinkCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkRepo extends JpaRepository<Drink, Long> {
    List<Drink> findDrinkByDrinkCategory(DrinkCategory drinkCategory);
    Optional<Drink> findDrinkByDrinkName(String drinkName);
}
