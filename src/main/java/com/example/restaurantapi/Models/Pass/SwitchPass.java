package com.example.restaurantapi.Models.Pass;

import com.example.restaurantapi.Models.Personal.Personal;
import jakarta.persistence.*;

@Entity
public class SwitchPass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long switchPassId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PassStatus passStatus = PassStatus.PENDING; // Standardvärde

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private Personal requester; // Den som begär bytet

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Personal receiver; // Den som får förfrågan

    @ManyToOne
    @JoinColumn(name = "pass_id", nullable = false)
    private Pass pass; // Passet som ska bytas

    public SwitchPass() {}

    public SwitchPass(Personal requester, Personal receiver, Pass pass) {
        this.requester = requester;
        this.receiver = receiver;
        this.pass = pass;
        this.passStatus = PassStatus.PENDING; // Standardstatus
    }

    // Getter och Setter
    public Long getSwitchPassId() { return switchPassId; }

    public PassStatus getPassStatus() { return passStatus; }

    public void setPassStatus(PassStatus passStatus) { this.passStatus = passStatus; }

    public Personal getRequester() { return requester; }

    public void setRequester(Personal requester) { this.requester = requester; }

    public Personal getReceiver() { return receiver; }

    public void setReceiver(Personal receiver) { this.receiver = receiver; }

    public Pass getPass() { return pass; }

    public void setPass(Pass pass) { this.pass = pass; }
}
