package com.example.restaurantapi.Models.Personal;

import com.example.restaurantapi.Models.Pass.Pass;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
//class: Personal

@Entity
public class Personal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long personalId;

    @Column(nullable = false)
    private String personalName;

    @ManyToMany(mappedBy = "personalPass")
    private List<Pass> passes;

    public Personal(Long id, String personalName) {
        this.personalId = id;
        this.personalName = personalName;
    }
    public Personal() {}

    public Long getPersonalId() {return personalId;}

    public void setPersonalId(Long personalId) {this.personalId = personalId;}

    public String getpersonalName() {return personalName;}

    public void setpersonalName(String personalName) {this.personalName = personalName;}
}
