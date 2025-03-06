package com.example.restaurantapi.Models.Pass;

import com.example.restaurantapi.Models.Personal.Personal;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

//class: Pass


@Entity
public class Pass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passId;

    @Column(nullable = false)
    private LocalDate passDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PassType passType;

    @ManyToMany
    @JoinTable(
            name = "pass_personal",
            joinColumns = @JoinColumn(name = "pass_id"),
            inverseJoinColumns = @JoinColumn(name = "personal_id")
    )
    @Column(nullable = false)
    private List<Personal> personalPass;

    public Pass() {}

    public Pass(Long passId, LocalDate passDateTime, PassType passType) {
        this.passId = passId;
        this.passDateTime = passDateTime;
        this.passType = passType;
    }

    public Long getPassId() { return passId; }

    public LocalDate getPassDateTime() { return passDateTime; }

    public void setPassDateTime(LocalDate passDateTime) { this.passDateTime = passDateTime; }

    public PassType getPassType() { return passType; }

    public void setPassType(PassType passType) { this.passType = passType; }

    public List<Personal> getPersonalPass() {
        return personalPass;
    }

    public void setPersonalPass(List<Personal> personalPass) {
        this.personalPass = personalPass;
    }
}
