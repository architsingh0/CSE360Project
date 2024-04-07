package com.solacecare.cse360project.nurse;

import com.solacecare.cse360project.generic.User;
import jakarta.persistence.*;

@Entity
public class Nurse extends User {
    private Long licenseNumber;

    public Nurse() {
    }

    public Nurse(String firstName, String lastName, String email, String password, Long licenseNumber) {
        super(firstName, lastName, email, password);
        this.licenseNumber = licenseNumber;
    }

    public Long getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(Long licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}