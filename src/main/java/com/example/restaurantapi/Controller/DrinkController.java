package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Drink.Drink;
import com.example.restaurantapi.Models.Drink.DrinkCategory;
import com.example.restaurantapi.Repo.DrinkRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drinks")
public class DrinkController {

    private final DrinkRepo drinkRepo;

    public DrinkController(DrinkRepo drinkRepo) {
        this.drinkRepo = drinkRepo;
    }

    // Hämta alla drycker
    @GetMapping("/")
    public List<Drink> getAllDrinks() {
        return drinkRepo.findAll();
    }

    // Hämta drycker efter kategori
    @GetMapping("/category/{drinkCategory}")
    public List<Drink> getDrinksByCategory(@PathVariable DrinkCategory drinkCategory) {
        return drinkRepo.findDrinkByDrinkCategory(drinkCategory);
    }

    // Hämta en specifik dryck via namn
    @GetMapping("/drink/{drinkName}")
    public Drink getDrink(@PathVariable String drinkName) {
        return drinkRepo.findDrinkByDrinkName(drinkName)
                .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + drinkName));
    }

    // Skapa en ny dryck
    @PostMapping("/")
    public String saveDrink(@RequestBody Drink drink) {
        try {
            drinkRepo.save(drink);
            return "Drink saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: A drink already exists with the name " + drink.getDrinkName();
        }
    }

    // Skapa flera drycker i batch
    @PostMapping("/batch")
    public String saveDrinks(@RequestBody List<Drink> drinks) {
        try {
            drinkRepo.saveAll(drinks);
            return "Drinks saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: Duplicate drink for the same name";
        }
    }

    // Uppdatera en dryck via namn
    @PutMapping("/drink/{drinkName}")
    public String updateDrink(@PathVariable String drinkName, @RequestBody Drink drink) {
        Drink updateDrink = drinkRepo.findDrinkByDrinkName(drinkName)
                .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + drinkName));

        // Uppdatera drycken
        updateDrink.setDrinkName(drink.getDrinkName());
        updateDrink.setDrinkPrice(drink.getDrinkPrice());

        drinkRepo.save(updateDrink);
        return "Drink updated successfully!";
    }

    // Ta bort en dryck via namn
    @DeleteMapping("/drink/{drinkName}")
    public String deleteDrink(@PathVariable String drinkName) {
        Drink drink = drinkRepo.findDrinkByDrinkName(drinkName)
                .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + drinkName));

        drinkRepo.delete(drink);
        return "Drink deleted successfully!";
    }
}
