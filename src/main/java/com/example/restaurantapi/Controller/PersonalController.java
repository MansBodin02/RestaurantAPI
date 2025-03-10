package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Personal.Personal;
import com.example.restaurantapi.Repo.PersonalRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//Controller: PersonalController



@RestController
@RequestMapping("/api/personal")
public class PersonalController {
    private final PersonalRepo personalRepo;

    public PersonalController(PersonalRepo personalRepo) {
        this.personalRepo = personalRepo;
    }

    // Hämta alla personal
    @GetMapping("/")
    public ResponseEntity<List<Personal>> getAllPersonal() {
        return ResponseEntity.ok(personalRepo.findAll());
    }

    @GetMapping("/{personalName}")
    public ResponseEntity<Personal> getPersonalByName(@PathVariable String personalName){
        return personalRepo.findByPersonalName(personalName).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }



    // Hämta en enskild personal baserat på ID
    @GetMapping("/{personalID}")
    public ResponseEntity<Personal> getPersonalById(@PathVariable Long personalID) {
        return personalRepo.findById(personalID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Skapa en personal
    @PostMapping("/")
    public ResponseEntity<String> savePersonal(@RequestBody Personal personal) {
        try {
            personalRepo.save(personal);
            return ResponseEntity.status(HttpStatus.CREATED).body("Personal Saved");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // Skapa flera personal samtidigt
    @PostMapping("/batch")
    public ResponseEntity<String> savePersonals(@RequestBody List<Personal> personals) {
        personalRepo.saveAll(personals);
        return ResponseEntity.status(HttpStatus.CREATED).body("Personal Saved");
    }

    // Ta bort en personal baserat på ID
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

