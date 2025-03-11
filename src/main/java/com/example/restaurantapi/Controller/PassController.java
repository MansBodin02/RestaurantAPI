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

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // Hämta alla pass
    @GetMapping("/")
    public ResponseEntity<List<Pass>> getAllPass() {
        return ResponseEntity.ok(passRepo.findAll());
    }

    // Hämta ett enskilt pass
    @GetMapping("/{passId}")
    public ResponseEntity<Pass> getPassById(@PathVariable Long passId) {
        return passRepo.findById(passId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @GetMapping("/requests/{receiverId}")
    public ResponseEntity<List<SwitchPass>> getPendingSwitchRequests(@PathVariable Long receiverId) {
        List<SwitchPass> requests = switchPassRepo.findByReceiverPersonalId(receiverId);

        if (requests.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(requests);
    }
/**
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Pass>> getPassByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        List<Pass> passes = passRepo.findByPassDateTimeBetween(startOfDay, endOfDay);
        return ResponseEntity.ok(passes);
    }
**/

    // Hämta alla pass av en viss typ (ex: MORGON, KVÄLL)
    @GetMapping("/type/{passType}")
    public ResponseEntity<List<Pass>> getPassByType(@PathVariable PassType passType) {
        List<Pass> passes = passRepo.findByPassType(passType);
        return ResponseEntity.ok(passes);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SwitchPass>> getRequestsByStatus(@PathVariable PassStatus status) {
        return ResponseEntity.ok(switchPassRepo.findByPassStatus(status));
    }

    @GetMapping("/requests/personal/{personalId}")
    public ResponseEntity<List<SwitchPass>> getSwitchRequestsForPersonal(@PathVariable Long personalId) {
        List<SwitchPass> requests = switchPassRepo.findByRequesterPersonalIdOrReceiverPersonalId(personalId, personalId);

        if (requests.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/personal/{personalId}")
    public ResponseEntity<List<Pass>> getPassesForPersonal(@PathVariable Long personalId) {
        Optional<Personal> personalOpt = personalRepo.findById(personalId);

        if (personalOpt.isPresent()) {
            List<Pass> passes = passRepo.findAll()
                    .stream()
                    .filter(pass -> pass.getPersonalPass().contains(personalOpt.get()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(passes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }



    @PostMapping("/")
    public ResponseEntity<String> createPass(@RequestBody Pass pass) {
        passRepo.save(pass);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pass created");
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestPassSwitch(
            @RequestParam Long requesterId,
            @RequestParam Long receiverId,
            @RequestParam Long passId) {

        Optional<Personal> requesterOpt = personalRepo.findById(requesterId);
        Optional<Personal> receiverOpt = personalRepo.findById(receiverId);
        Optional<Pass> passOpt = passRepo.findById(passId);

        if (!passOpt.get().getPersonalPass().contains(requesterOpt.get())) {
            return ResponseEntity.badRequest().body("Requester does not own this pass!");
        }

        boolean exists = switchPassRepo.existsByRequesterAndReceiverAndPass(requesterOpt.get(), receiverOpt.get(), passOpt.get());
        if (exists) {
            return ResponseEntity.badRequest().body("Request already exists!");
        }


        if (requesterOpt.isPresent() && receiverOpt.isPresent() && passOpt.isPresent()) {
            SwitchPass switchPass = new SwitchPass(requesterOpt.get(), receiverOpt.get(), passOpt.get());
            switchPassRepo.save(switchPass);
            return ResponseEntity.ok("Pass request sent!");
        } else {
            return ResponseEntity.badRequest().body("Invalid requester, receiver, or pass ID.");
        }
    }

    // Koppla en personal till ett pass
    @PutMapping("/{passId}/assign/{personalId}")
    public ResponseEntity<String> assignPersonalToPass(@PathVariable Long passId, @PathVariable Long personalId) {
        Optional<Pass> optionalPass = passRepo.findById(passId);
        Optional<Personal> optionalPersonal = personalRepo.findById(personalId);

        if (optionalPass.isPresent() && optionalPersonal.isPresent()) {
            Pass pass = optionalPass.get();
            Personal personal = optionalPersonal.get();

            pass.getPersonalPass().add(personal);
            passRepo.save(pass);

            return ResponseEntity.ok("Personal assigned to pass");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pass or Personal not found");
        }
    }

    @PutMapping("/{passId}/remove/{personalId}")
    public ResponseEntity<String> removePersonalFromPass(@PathVariable Long passId, @PathVariable Long personalId) {
        Optional<Pass> optionalPass = passRepo.findById(passId);
        Optional<Personal> optionalPersonal = personalRepo.findById(personalId);

        if (optionalPass.isPresent() && optionalPersonal.isPresent()) {
            Pass pass = optionalPass.get();
            Personal personal = optionalPersonal.get();

            if (pass.getPersonalPass().contains(personal)) {
                pass.getPersonalPass().remove(personal);
                passRepo.save(pass);
                return ResponseEntity.ok("Personal removed from pass");
            } else {
                return ResponseEntity.badRequest().body("Personal is not assigned to this pass");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pass or Personal not found");
        }
    }

    @Transactional
    @PutMapping("/{switchPassId}/update-status")
    public ResponseEntity<String> updatePassSwitchStatus(
            @PathVariable Long switchPassId,
            @RequestParam PassStatus status) {

        Optional<SwitchPass> switchPassOpt = switchPassRepo.findById(switchPassId);

        if (switchPassOpt.isPresent()) {
            SwitchPass switchPass = switchPassOpt.get();

            if (status == PassStatus.APPROVED) {
                // Om passbytet godkänns -> Uppdatera passets personal
                Pass pass = switchPass.getPass();
                pass.getPersonalPass().remove(switchPass.getRequester()); // Ta bort den gamla personen
                pass.getPersonalPass().add(switchPass.getReceiver()); // Lägg till den nya personen
                passRepo.save(pass);

                switchPass.setPassStatus(status);
                switchPassRepo.save(switchPass);
                return ResponseEntity.ok("Pass switch approved.");
            } else if (status == PassStatus.DECLINED) {
                // Om passbytet nekas -> Ta bort SwitchPass från databasen
                //switchPassRepo.delete(switchPass);
                return ResponseEntity.ok("Pass switch declined and removed.");
            }

            return ResponseEntity.badRequest().body("Invalid status provided.");
        } else {
            return ResponseEntity.badRequest().body("Switch request not found.");
        }
    }

    @Transactional
    @DeleteMapping("/{passId}")
    public ResponseEntity<String> deletePass(@PathVariable Long passId) {
        Optional<Pass> optionalPass = passRepo.findById(passId);

        if (optionalPass.isPresent()) {
            Pass pass = optionalPass.get();

            List<SwitchPass> switchPasses = switchPassRepo.findByPass(pass);
            if (!switchPasses.isEmpty()) {
                switchPassRepo.deleteAll(switchPasses);
            }

            passRepo.delete(pass);

            return ResponseEntity.ok("Pass och associerade SwitchPass-poster har raderats");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Passet kunde inte hittas");
        }
    }

}

