package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Drink.Drink;
import com.example.restaurantapi.Models.Drink.DrinkCategory;
import com.example.restaurantapi.Repo.DrinkRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DrinkController - A REST API controller for managing drinks in the restaurant system.
 * Provides endpoints to create, retrieve, update, and delete drinks.
 */
@RestController
@RequestMapping("/api/drinks")
public class DrinkController {

    private final DrinkRepo drinkRepo;

    public DrinkController(DrinkRepo drinkRepo) {
        this.drinkRepo = drinkRepo;
    }

    /**
     * Retrieve all drinks from the database.
     * @return List of all available drinks.
     */
    @GetMapping("/")
    public List<Drink> getAllDrinks() {
        return drinkRepo.findAll();
    }

    /**
     * Retrieve drinks based on their category.
     * @param drinkCategory The category of drinks to filter by.
     * @return List of drinks matching the specified category.
     */
    @GetMapping("/category/{drinkCategory}")
    public List<Drink> getDrinksByCategory(@PathVariable DrinkCategory drinkCategory) {
        return drinkRepo.findDrinkByDrinkCategory(drinkCategory);
    }

    /**
     * Retrieve a specific drink by its name.
     * @param drinkName The name of the drink.
     * @return The drink object if found.
     */
    @GetMapping("/drink/{drinkName}")
    public Drink getDrink(@PathVariable String drinkName) {
        return drinkRepo.findDrinkByDrinkName(drinkName)
                .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + drinkName));
    }

    /**
     * Save a new drink to the database.
     * @param drink The drink object to be saved.
     * @return Success or error message.
     */
    @PostMapping("/")
    public String saveDrink(@RequestBody Drink drink) {
        try {
            drinkRepo.save(drink);
            return "Drink saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: A drink already exists with the name " + drink.getDrinkName();
        }
    }

    /**
     * Save multiple drinks to the database in a batch operation.
     * @param drinks List of drinks to be saved.
     * @return Success or error message.
     */
    @PostMapping("/batch")
    public String saveDrinks(@RequestBody List<Drink> drinks) {
        try {
            drinkRepo.saveAll(drinks);
            return "Drinks saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: Duplicate drink for the same name";
        }
    }

    /**
     * Update an existing drink by its name.
     * @param drinkName The name of the drink to be updated.
     * @param drink The new drink details.
     * @return Success message.
     */
    @PutMapping("/drink/{drinkName}")
    public String updateDrink(@PathVariable String drinkName, @RequestBody Drink drink) {
        Drink updateDrink = drinkRepo.findDrinkByDrinkName(drinkName)
                .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + drinkName));

        // Update drink details
        updateDrink.setDrinkName(drink.getDrinkName());
        updateDrink.setDrinkPrice(drink.getDrinkPrice());

        drinkRepo.save(updateDrink);
        return "Drink updated successfully!";
    }

    /**
     * Delete a drink from the database by its name.
     * @param drinkName The name of the drink to be deleted.
     * @return Success message.
     */
    @DeleteMapping("/drink/{drinkName}")
    public String deleteDrink(@PathVariable String drinkName) {
        Drink drink = drinkRepo.findDrinkByDrinkName(drinkName)
                .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + drinkName));

        drinkRepo.delete(drink);
        return "Drink deleted successfully!";
    }
}
