package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Order.CustomerOrder;
import com.example.restaurantapi.Models.Pass.Pass;
import com.example.restaurantapi.Models.Personal.Personal;
import com.example.restaurantapi.Repo.PassRepo;
import com.example.restaurantapi.Repo.PersonalRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pass")
public class PassController {
    private final PassRepo passRepo;
    private final PersonalRepo personalRepo;

    public PassController(PassRepo passRepo, PersonalRepo personalRepo) {
        this.passRepo = passRepo;
        this.personalRepo = personalRepo;
    }

    @GetMapping("/")
    public List<Pass> getAllPass() {
        return passRepo.findAll();
    }

    @PostMapping("/")
    public String savePass(@RequestBody Pass pass) {
        List<Personal> personalList = pass.getPersonalPass().stream()
                .map(personal -> {
                    Optional<Personal> existingPersonal = personalRepo.findByPersonalName((personal.getpersonalName()));
                    return existingPersonal.orElseGet(() -> {

                        Personal newPersonal = new Personal();
                        newPersonal.setpersonalName(personal.getpersonalName());
                        return personalRepo.save(newPersonal);
                    });
                })
                .collect(Collectors.toList());

        pass.setPersonalPass(personalList);

        // Spara passet
        passRepo.save(pass);
        return "Pass Saved";
    }
}
