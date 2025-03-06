package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Lunch.Lunch;
import com.example.restaurantapi.Repo.LunchRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * LunchController - A REST API controller for managing lunch menus in the restaurant system.
 * Provides endpoints to create, retrieve, update, and delete lunch entries.
 */
@RestController
@RequestMapping("/api/lunches")
public class LunchController {
    private final LunchRepo lunchRepo;

    public LunchController(LunchRepo lunchRepo) {
        this.lunchRepo = lunchRepo;
    }

    /**
     * Retrieve all lunches from the database.
     * @return List of all available lunches.
     */
    @GetMapping("/")
    public List<Lunch> getAllLunches() {
        return lunchRepo.findAll();
    }

    /**
     * Retrieve lunches by a specific week.
     * @param lunchWeek The week number.
     * @return List of lunches for the specified week.
     */
    @GetMapping("/{lunchWeek}")
    public List<Lunch> getLunchesByWeek(@PathVariable int lunchWeek) {
        return lunchRepo.findByLunchWeek(lunchWeek);
    }

    /**
     * Retrieve a specific lunch by week and day.
     * @param lunchWeek The week number.
     * @param lunchDay The day of the week.
     * @return The lunch object if found.
     */
    @GetMapping("/{lunchWeek}/{lunchDay}")
    public Lunch getLunchByDay(@PathVariable int lunchWeek, @PathVariable int lunchDay) {
        return lunchRepo.findByLunchWeekAndLunchDay(lunchWeek, lunchDay)
                .orElseThrow(() -> new RuntimeException("Lunch not found for week " + lunchWeek + ", day " + lunchDay));
    }

    /**
     * Save a new lunch entry to the database.
     * @param lunch The lunch object to be saved.
     * @return Success or error message.
     */
    @PostMapping("/")
    public String saveLunch(@RequestBody Lunch lunch) {
        try {
            lunchRepo.save(lunch);
            return "Lunch saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: Duplicate lunch for the same week and day.";
        }
    }

    /**
     * Save multiple lunches to the database in a batch operation.
     * @param lunches List of lunches to be saved.
     * @return Success or error message.
     */
    @PostMapping("/batch")
    public String saveLunches(@RequestBody List<Lunch> lunches) {
        try {
            lunchRepo.saveAll(lunches);
            return "Lunches saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: Duplicate lunch for the same week and day.";
        }
    }

    /**
     * Update an existing lunch entry by week and day.
     * @param lunchWeek The week number.
     * @param lunchDay The day of the week.
     * @param lunch The new lunch details.
     * @return Success message.
     */
    @PutMapping("/{lunchWeek}/{lunchDay}")
    public String updateLunch(@PathVariable int lunchWeek, @PathVariable int lunchDay, @RequestBody Lunch lunch) {
        Optional<Lunch> optionalLunch = lunchRepo.findByLunchWeekAndLunchDay(lunchWeek, lunchDay);
        if (optionalLunch.isEmpty()) {
            return "Lunch not found for week " + lunchWeek + ", day " + lunchDay;
        }

        Lunch updateLunch = optionalLunch.get();
        updateLunch.setLunchName(lunch.getLunchName());
        updateLunch.setLunchWeek(lunch.getLunchWeek());
        updateLunch.setLunchDay(lunch.getLunchDay());
        updateLunch.setLunchDescription(lunch.getLunchDescription());
        updateLunch.setLunchPrice(lunch.getLunchPrice());

        lunchRepo.save(updateLunch);
        return "Lunch updated successfully!";
    }

    /**
     * Delete all lunches for a specific week.
     * @param lunchWeek The week number.
     * @return Success or error message.
     */
    @DeleteMapping("/{lunchWeek}")
    public String deleteLunch(@PathVariable int lunchWeek) {
        List<Lunch> lunches = lunchRepo.findByLunchWeek(lunchWeek);

        if (lunches.isEmpty()) {
            return "Lunches not found for week " + lunchWeek;
        }

        // Delete all lunches for the given week
        lunchRepo.deleteAll(lunches);
        return "Lunches deleted successfully!";
    }

    /**
     * Delete a specific lunch by week and day.
     * @param lunchWeek The week number.
     * @param lunchDay The day of the week.
     * @return Success or error message.
     */
    @DeleteMapping("/{lunchWeek}/{lunchDay}")
    public String deleteLunch(@PathVariable int lunchWeek, @PathVariable int lunchDay) {
        Optional<Lunch> optionalLunch = lunchRepo.findByLunchWeekAndLunchDay(lunchWeek, lunchDay);
        if (optionalLunch.isEmpty()) {
            return "Lunch not found for week " + lunchWeek + ", day " + lunchDay;
        }

        lunchRepo.delete(optionalLunch.get());
        return "Lunch deleted successfully!";
    }
}