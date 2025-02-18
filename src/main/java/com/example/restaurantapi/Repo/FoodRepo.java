
package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Food.Food;
import com.example.restaurantapi.Models.Food.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodRepo extends JpaRepository<Food, Long> {
    List<Food> findFoodByFoodCategory(FoodCategory foodCategory);
    Optional<Food> findFoodByFoodName(String foodName);
    Optional<Food> findFoodByFoodId(Long foodId);
}