package com.solacecare.cse360project.patient;

import jakarta.persistence.*;

@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pharmacyAddr;
    private String medicineName;
    private String medicineDosage;
    private String drSignature;

    @ManyToOne
    @JoinColumn(name = "patient_visit_id")
    private PatientVisit patientVisit;

    public Prescription() {
    }

    public Prescription(String pharmacyAddr, String medicineName, String medicineDosage, String drSignature) {
        this.pharmacyAddr = pharmacyAddr;
        this.medicineName = medicineName;
        this.medicineDosage = medicineDosage;
        this.drSignature = drSignature;
    }

    public String getPharmacyAddr() {
        return pharmacyAddr;
    }

    public void setPharmacyAddr(String pharmacyAddr) {
        this.pharmacyAddr = pharmacyAddr;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineDosage() {
        return medicineDosage;
    }

    public void setMedicineDosage(String medicineDosage) {
        this.medicineDosage = medicineDosage;
    }

    public String getDrSignature() {
        return drSignature;
    }

    public void setDrSignature(String drSignature) {
        this.drSignature = drSignature;
    }

    public PatientVisit getPatientVisit() {
        return patientVisit;
    }

    public void setPatientVisit(PatientVisit patientVisit) {
        this.patientVisit = patientVisit;
    }

    @Override
    public String toString() {
        return String.format("Medicine: %s, Dosage: %s, Pharmacy: %s", medicineName, medicineDosage, pharmacyAddr);
    }

}
