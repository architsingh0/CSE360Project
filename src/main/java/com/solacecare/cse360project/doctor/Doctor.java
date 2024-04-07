package com.solacecare.cse360project.doctor;

import com.solacecare.cse360project.generic.User;
import jakarta.persistence.*;

@Entity
public class Doctor extends User {
    private Long licenseNumber;

    public Doctor() {
    }

    public Doctor(String firstName, String lastName, String email, String password, Long licenseNumber) {
        super(firstName, lastName, email, password);
        this.licenseNumber = licenseNumber;
    }

    public Long getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(Long licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String toString() {
        return "Doctor{" +
                "licenseNumber=" + licenseNumber +
                '}';
    }
}