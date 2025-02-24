package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Pass.Pass;
import com.example.restaurantapi.Repo.PassRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//Controller: PassController



@RestController
@RequestMapping("/api/pass")
public class PassController {
    private final PassRepo passRepo;

    public PassController(PassRepo passRepo) {
        this.passRepo = passRepo;
    }

    @GetMapping("/")
    public List<Pass> getAllPass() {
        return passRepo.findAll();
    }

    @PostMapping("/")
    public String savePass(@RequestBody Pass pass) {
        passRepo.save(pass);
        return "Pass Saved";
    }

    @PostMapping("/batch")
    public String savePasses(@RequestBody List<Pass> passes) {
        passRepo.saveAll(passes);
        return "Pass Saved";
    }
}
