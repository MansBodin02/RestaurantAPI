package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Food.Food;
import com.example.restaurantapi.Models.Food.FoodCategory;
import com.example.restaurantapi.Repo.FoodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodRepo foodRepo;

    @GetMapping("/")
    public List<Food> getAllFoods() {
        return foodRepo.findAll();
    }

    @GetMapping("/Category/{foodCategory}")
    public List<Food> getFoodsByCategory(@PathVariable FoodCategory foodCategory) {
        return foodRepo.findFoodByFoodCategory(foodCategory);
    }

    @GetMapping("food/{foodName}")
    public Food getFood(@PathVariable String foodName) {
        return foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));
    }

    @PostMapping("/")
    public String saveFood(@RequestBody Food food) {
        try {
            foodRepo.save(food);
            return "Food saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: A food already exists for name " + food.getFoodName();
        }
    }

    @PostMapping("/batch")
    public String saveFoods(@RequestBody List<Food> foods) {
        try {
            foodRepo.saveAll(foods);
            return "Foods saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: Duplicate food for the same name";
        }
    }

    @PutMapping("food/{foodName}")
    public String updateFood(@PathVariable String foodName, @RequestBody Food food) {
        Food updateFood = foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));

        // Uppdatera maten
        updateFood.setFoodName(food.getFoodName());
        updateFood.setFoodType(food.getFoodType());
        updateFood.setFoodCategory(food.getFoodCategory());
        updateFood.setFoodDescription(food.getFoodDescription());
        updateFood.setFoodPrice(food.getFoodPrice());

        foodRepo.save(updateFood);
        return "Food updated successfully!";
    }

    @DeleteMapping("food/{foodName}")
    public String deleteFood(@PathVariable String foodName) {
        Food food = foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));

        foodRepo.delete(food);
        return "Food deleted successfully!";
    }
}
