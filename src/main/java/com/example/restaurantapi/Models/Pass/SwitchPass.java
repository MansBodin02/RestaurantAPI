package com.example.restaurantapi.Models.Pass;
import com.example.restaurantapi.Models.Personal.Personal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class SwitchPass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long switchPassId;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PassStatus passStatus;

    @ManyToOne
    @JoinColumn(name = "pass_id", nullable = false)
    @JsonBackReference
    private Pass pass;

    @ManyToOne
    @JoinColumn(name = "personal_id")
    private Personal personal;

    public SwitchPass() {}

    public PassStatus getPassStatus() {return passStatus;}

    public void setPassStatus(PassStatus passStatus) {this.passStatus = passStatus;}

    public Pass getPass() {return pass;}

    public void setPass(Pass pass) {this.pass = pass;}

    public Personal getPersonal() {return personal;}

    public void setPersonal(Personal personal) {this.personal = personal;}
}
