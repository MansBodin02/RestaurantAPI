package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Lunch.Lunch;
import com.example.restaurantapi.Repo.LunchRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lunches")
public class LunchController {
    private final LunchRepo lunchRepo;

    public LunchController(LunchRepo lunchRepo) {
        this.lunchRepo = lunchRepo;
    }

    @GetMapping("/")
    public List<Lunch> getAllLunches() {
        return lunchRepo.findAll();
    }

    @GetMapping("/{lunchWeek}")
    public List<Lunch> getLunchesByWeek(@PathVariable int lunchWeek) {
        return lunchRepo.findByLunchWeek(lunchWeek);
    }

    @GetMapping("/{lunchWeek}/{lunchDay}")
    public Lunch getLunchByDay(@PathVariable int lunchWeek, @PathVariable int lunchDay) {
        return lunchRepo.findByLunchWeekAndLunchDay(lunchWeek, lunchDay)
                .orElseThrow(() -> new RuntimeException("Lunch not found for week " + lunchWeek + ", day " + lunchDay));
    }

    @PostMapping("/")
    public String saveLunch(@RequestBody Lunch lunch) {
        try {
            lunchRepo.save(lunch);
            return "Lunch saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: A lunch already exists for week " + lunch.getLunchWeek() + " and day " + lunch.getLunchDay();
        }
    }

    @PostMapping("/batch")
    public String saveLunches(@RequestBody List<Lunch> lunches) {
        try {
            lunchRepo.saveAll(lunches);
            return "Lunches saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: Duplicate lunch for the same week and day.";
        }
    }


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

    @DeleteMapping("/{lunchWeek}")
    public String deleteLunch(@PathVariable int lunchWeek) {
        List<Lunch> lunches = lunchRepo.findByLunchWeek(lunchWeek);

        if (lunches.isEmpty()) {
            return "Lunches not found for week " + lunchWeek;
        }

        // Ta bort alla lunches i listan
        lunchRepo.deleteAll(lunches);

        return "Lunches deleted successfully!";
    }

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
