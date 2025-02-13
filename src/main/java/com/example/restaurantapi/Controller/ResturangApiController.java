package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Lunch;
import com.example.restaurantapi.Repo.LunchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ResturangApiController {
    @Autowired
    private LunchRepo lunchRepo;
    @GetMapping(value = "/")
    public String getPage() {
        return "Welcome";
    }
    @GetMapping(value = "/lunches")
    public List<Lunch> getLunches() {
        return lunchRepo.findAll();
    }

    @GetMapping(value = "/lunches/{week}")
    public List<Lunch> getLunchesByWeek(@PathVariable int week) {
        return lunchRepo.findByLunchWeek(week);
    }

    @GetMapping(value = "/lunches/{week}/{day}")
    public Lunch getLunchByDay(@PathVariable int week, @PathVariable int day) {
        Optional<Lunch> optionalLunch = lunchRepo.findByLunchWeekAndLunchDay(week, day);
        if (optionalLunch.isPresent()) {
            return optionalLunch.get();
        } else {
            throw new RuntimeException("Lunch not found for the specified week and day");
        }
    }



    @PostMapping(value = "/save")
    public String saveLunch(@RequestBody Lunch lunch) {
        lunchRepo.save(lunch);
        return "Saved...";
    }

    @PutMapping(value = "/update/{week}/{day}")
    public String updateLunch(@PathVariable int week, @PathVariable int day, @RequestBody Lunch lunch) {
        // Hämta en lunch baserat på vecka och dag
        Optional<Lunch> optionalLunch = lunchRepo.findByLunchWeekAndLunchDay(week, day);

        if (optionalLunch.isEmpty()) {
            return "Lunch not found for the specified week and day";
        }

        Lunch updateLunch = optionalLunch.get(); // Hämta den första (och enda) lunchen

        // Uppdatera lunch med värden från requestBody
        updateLunch.setLunchName(lunch.getLunchName());
        updateLunch.setLunchWeek(lunch.getLunchWeek());
        updateLunch.setLunchDay(lunch.getLunchDay());

        // Spara den uppdaterade lunchinformationen
        lunchRepo.save(updateLunch);

        return "Lunch updated successfully";
    }

    @DeleteMapping(value = "/delete/{week}/{day}")
    public String deleteLunch(@PathVariable int week, @PathVariable int day) {
        Optional<Lunch> optionalLunch = lunchRepo.findByLunchWeekAndLunchDay(week, day);
        if (optionalLunch.isEmpty()) {
            return "Lunch not found for the specified week and day";
        }

        Lunch deleteLunch = optionalLunch.get(); // Hämta den första (och enda) lunchen

        lunchRepo.delete(deleteLunch);
        return "Lunch deleted";
    }

}
