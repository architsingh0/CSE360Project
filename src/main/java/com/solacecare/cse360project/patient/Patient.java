package com.solacecare.cse360project.patient;

import com.solacecare.cse360project.generic.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Patient extends User {
    private LocalDate dateOfBirth;
    private Long insuranceNum;
    private String phoneNum;
    private String pharmacyAddr;
    private String patientIdentifier;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientVisit> visits = new ArrayList<>();

    public Patient() {
    }

    public Patient(String firstName, String lastName, String email, String password, LocalDate dateOfBirth, Long insuranceNum, String phoneNum, String pharmacyAddr) {
        super(firstName, lastName, email, password);
        this.dateOfBirth = dateOfBirth;
        this.insuranceNum = insuranceNum;
        this.phoneNum = phoneNum;
        this.pharmacyAddr = pharmacyAddr;
        this.patientIdentifier = firstName.substring(0,3) + lastName.substring(0,3) + dateOfBirth.getMonthValue() + dateOfBirth.getDayOfMonth() + dateOfBirth.getYear();
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getInsuranceNum() {
        return insuranceNum;
    }

    public void setInsuranceNum(Long insuranceNum) {
        this.insuranceNum = insuranceNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPharmacyAddr() {
        return pharmacyAddr;
    }

    public void setPharmacyAddr(String pharmacyAddr) {
        this.pharmacyAddr = pharmacyAddr;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    public List<PatientVisit> getVisits() {
        return visits;
    }

    public void setVisits(List<PatientVisit> visits) {
        this.visits = visits;
    }
}