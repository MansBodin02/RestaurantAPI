package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Personal.Personal;
import com.example.restaurantapi.Repo.PersonalRepo;
import org.springframework.dao.DataIntegrityViolationException;
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

    @GetMapping("/")
    public List<Personal> getAllPersonal() {
        return personalRepo.findAll();
    }

    @PostMapping("/")
    public String savePersonal(@RequestBody Personal personal) {
        try {
            personalRepo.save(personal);
            return "Personal Saved";
        } catch (DataIntegrityViolationException e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/batch")
    public String savePersonals(@RequestBody List<Personal> personals) {
        personalRepo.saveAll(personals);
        return "Personal Saved";
    }

}
