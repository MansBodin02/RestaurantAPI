package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Pass.Pass;
import com.example.restaurantapi.Models.Pass.PassStatus;
import com.example.restaurantapi.Models.Pass.PassType;
import com.example.restaurantapi.Models.Pass.SwitchPass;
import com.example.restaurantapi.Models.Personal.Personal;
import com.example.restaurantapi.Repo.PassRepo;
import com.example.restaurantapi.Repo.PersonalRepo;
import com.example.restaurantapi.Repo.SwitchPassRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * PassController - A REST API controller for managing work shifts (passes) in the restaurant system.
 * Provides endpoints to create, retrieve, update, assign, and delete work passes.
 */
@RestController
@RequestMapping("/api/pass")
public class PassController {
    private final PassRepo passRepo;
    private final PersonalRepo personalRepo;
    private final SwitchPassRepo switchPassRepo;

    public PassController(PassRepo passRepo, PersonalRepo personalRepo, SwitchPassRepo switchPassRepo) {
        this.passRepo = passRepo;
        this.personalRepo = personalRepo;
        this.switchPassRepo = switchPassRepo;
    }

    /**
     * Retrieve all passes from the database.
     * @return List of all passes.
     */
    @GetMapping("/")
    public ResponseEntity<List<Pass>> getAllPass() {
        return ResponseEntity.ok(passRepo.findAll());
    }

    /**
     * Retrieve a specific pass by its ID.
     * @param passId The ID of the pass.
     * @return The pass if found.
     */
    @GetMapping("/{passId}")
    public ResponseEntity<Pass> getPassById(@PathVariable Long passId) {
        return passRepo.findById(passId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Delete a pass by its ID.
     * @param passId The ID of the pass to delete.
     * @return Success or error message.
     */
    @DeleteMapping("/{passId}")
    public ResponseEntity<String> deletePass(@PathVariable Long passId) {
        if (passRepo.existsById(passId)) {
            passRepo.deleteById(passId);
            return ResponseEntity.ok("Pass deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pass not found");
        }
    }
}