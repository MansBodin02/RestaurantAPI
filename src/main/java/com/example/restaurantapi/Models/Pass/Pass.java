package com.example.restaurantapi.Models.Pass;

import com.example.restaurantapi.Models.Personal.Personal;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

//class: Pass


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
    @Column(nullable = false)
    private List<Personal> personalPass;

    public Pass() {}

    public Pass(Long passId, LocalDateTime passDateTime, PassType passType) {
        this.passId = passId;
        this.passDateTime = passDateTime;
        this.passType = passType;
    }

    public Long getPassId() { return passId; }

    public LocalDateTime getPassDateTime() { return passDateTime; }

    public void setPassDateTime(LocalDateTime passDateTime) { this.passDateTime = passDateTime; }

    public PassType getPassType() { return passType; }

    public void setPassType(PassType passType) { this.passType = passType; }

    public List<Personal> getPersonalPass() {
        return personalPass;
    }

    public void setPersonalPass(List<Personal> personalPass) {
        this.personalPass = personalPass;
    }
}
