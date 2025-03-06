package com.example.restaurantapi.Models.Personal;

import com.example.restaurantapi.Models.Pass.Pass;
import jakarta.persistence.*;

import java.util.List;
//class: Personal

@Entity
public class Personal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalId;

    @Column(nullable = false)
    private String personalName;

    @Column(nullable = false)
    private String deviceNumber;

    @ManyToMany(mappedBy = "personalPass")
    private List<Pass> passes;

    public Personal() {
    }

    public Personal(Long id, String personalName, String deviceNumber) {
        this.personalId = id;
        this.personalName = personalName;
        this.deviceNumber = deviceNumber;
    }

    public Long getPersonalId() { return personalId; }

    public void setPersonalId(Long personalId) { this.personalId = personalId; }

    public String getPersonalName() { return personalName; }

    public void setPersonalName(String personalName) { this.personalName = personalName; }

    public String getDeviceNumber() {return deviceNumber;}

    public void setDeviceNumber(String deviceNumber) {this.deviceNumber = deviceNumber;}
}
