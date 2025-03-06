package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Personal.Personal;
import com.example.restaurantapi.Repo.PersonalRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PersonalController - A REST API controller for managing personal (staff) in the restaurant system.
 * Provides endpoints to create, retrieve, and delete personal entries.
 */
@RestController
@RequestMapping("/api/personal")
public class PersonalController {
    private final PersonalRepo personalRepo;

    public PersonalController(PersonalRepo personalRepo) {
        this.personalRepo = personalRepo;
    }

    /**
     * Retrieve all personal from the database.
     * @return List of all personal.
     */
    @GetMapping("/")
    public ResponseEntity<List<Personal>> getAllPersonal() {
        return ResponseEntity.ok(personalRepo.findAll());
    }

    /**
     * Retrieve a specific personal by ID.
     * @param personalID The ID of the personal.
     * @return The personal if found.
     */
    @GetMapping("/{personalID}")
    public ResponseEntity<Personal> getPersonalById(@PathVariable Long personalID) {
        return personalRepo.findById(personalID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Save a new personal to the database.
     * @param personal The personal object to be saved.
     * @return Success or error message.
     */
    @PostMapping("/")
    public ResponseEntity<String> savePersonal(@RequestBody Personal personal) {
        try {
            personalRepo.save(personal);
            return ResponseEntity.status(HttpStatus.CREATED).body("Personal Saved");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Save multiple personal entries to the database.
     * @param personals List of personal objects to be saved.
     * @return Success message.
     */
    @PostMapping("/batch")
    public ResponseEntity<String> savePersonals(@RequestBody List<Personal> personals) {
        personalRepo.saveAll(personals);
        return ResponseEntity.status(HttpStatus.CREATED).body("Personal Saved");
    }

    /**
     * Delete a personal by ID.
     * @param personalID The ID of the personal to delete.
     * @return Success or error message.
     */
    @DeleteMapping("/{personalID}")
    public ResponseEntity<String> deletePersonal(@PathVariable Long personalID) {
        if (personalRepo.existsById(personalID)) {
            personalRepo.deleteById(personalID);
            return ResponseEntity.ok("Personal deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personal not found");
        }
    }
}
