package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Food.Food;
import com.example.restaurantapi.Models.Food.FoodCategory;
import com.example.restaurantapi.Repo.FoodRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FoodController - A REST API controller for managing food items in the restaurant system.
 * Provides endpoints to create, retrieve, update, and delete food items.
 */
@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodRepo foodRepo;

    public FoodController(FoodRepo foodRepo) {
        this.foodRepo = foodRepo;
    }

    /**
     * Retrieve all food items from the database.
     * @return List of all available food items.
     */
    @GetMapping("/")
    public List<Food> getAllFoods() {
        return foodRepo.findAll();
    }

    /**
     * Retrieve food items based on their category.
     * @param foodCategory The category of food to filter by.
     * @return List of food items matching the specified category.
     */
    @GetMapping("/Category/{foodCategory}")
    public List<Food> getFoodsByCategory(@PathVariable FoodCategory foodCategory) {
        return foodRepo.findFoodByFoodCategory(foodCategory);
    }

    /**
     * Retrieve a specific food item by its name.
     * @param foodName The name of the food item.
     * @return The food object if found.
     */
    @GetMapping("food/{foodName}")
    public Food getFood(@PathVariable String foodName) {
        return foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));
    }

    /**
     * Save a new food item to the database.
     * @param food The food object to be saved.
     * @return Success or error message.
     */
    @PostMapping("/")
    public String saveFood(@RequestBody Food food) {
        try {
            foodRepo.save(food);
            return "Food saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: A food already exists for name " + food.getFoodName();
        }
    }

    /**
     * Save multiple food items to the database in a batch operation.
     * @param foods List of food items to be saved.
     * @return Success or error message.
     */
    @PostMapping("/batch")
    public String saveFoods(@RequestBody List<Food> foods) {
        try {
            foodRepo.saveAll(foods);
            return "Foods saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: Duplicate food for the same name";
        }
    }

    /**
     * Update an existing food item by its name.
     * @param foodName The name of the food item to be updated.
     * @param food The new food details.
     * @return Success message.
     */
    @PutMapping("food/{foodName}")
    public String updateFood(@PathVariable String foodName, @RequestBody Food food) {
        Food updateFood = foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));

        // Update food details
        updateFood.setFoodName(food.getFoodName());
        updateFood.setFoodType(food.getFoodType());
        updateFood.setFoodCategory(food.getFoodCategory());
        updateFood.setFoodDescription(food.getFoodDescription());
        updateFood.setFoodPrice(food.getFoodPrice());

        foodRepo.save(updateFood);
        return "Food updated successfully!";
    }

    /**
     * Delete a food item from the database by its name.
     * @param foodName The name of the food item to be deleted.
     * @return Success message.
     */
    @DeleteMapping("food/{foodName}")
    public String deleteFood(@PathVariable String foodName) {
        Food food = foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));

        foodRepo.delete(food);
        return "Food deleted successfully!";
    }
}