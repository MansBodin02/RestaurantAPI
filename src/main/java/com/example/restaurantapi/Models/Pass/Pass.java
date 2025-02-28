package com.example.restaurantapi.Models.Pass;

import com.example.restaurantapi.Models.Personal.Personal;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Pass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passId;

    @Column(nullable = false)
    private LocalDateTime passDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PassType passType;

    @ManyToMany
    @JoinTable(
            name = "pass_personal",
            joinColumns = @JoinColumn(name = "pass_id"),
            inverseJoinColumns = @JoinColumn(name = "personal_id")
    )
    private List<Personal> personalPass;

    public Pass(Long passId, LocalDateTime passDateTime, PassType passType) {
        this.passId = passId;
        this.passDateTime = passDateTime;
        this.passType = passType;
    }

    public Pass() {}

    public Long getPassId() { return passId; }

    public void setPassId(Long passId) { this.passId = passId; }

    public LocalDateTime getPassDateTime() { return passDateTime; }

    public void setPassDateTime(LocalDateTime passDateTime) { this.passDateTime = passDateTime; }

    public PassType getPassType() { return passType; }

    public void setPassType(PassType passType) { this.passType = passType; }

    public List<Personal> getPersonalPass() { return personalPass; }

    public void setPersonalPass(List<Personal> personalPass) { this.personalPass = personalPass; }

    // Ny metod för att endast returnera personalName istället för hela Personal-objektet
    @JsonProperty("personalPass")
    public List<String> getPersonalPassNames() {
        return personalPass.stream()
                .map(Personal::getpersonalName)  // Konvertera varje Personal till dess namn
                .collect(Collectors.toList());
    }
}
