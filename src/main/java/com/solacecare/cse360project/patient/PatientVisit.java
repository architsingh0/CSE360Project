package com.solacecare.cse360project.patient;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PatientVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateOfBirth;
    private LocalDate date;
    private LocalTime time;
    private String patientIdentifier;
    private double weight;
    private double height;
    private double bodyTemp;
    private double bloodPressure;
    private String immunizationRecord;
    private String nurseNotes;
    private String symptoms;
    private String drNotes;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @OneToMany(mappedBy = "patientVisit", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Prescription> prescriptions = new ArrayList<>();
    public PatientVisit() {
    }

    public PatientVisit(LocalDate dateOfBirth, LocalDate date, LocalTime time, String patientIdentifier, double weight, double height, double bodyTemp, double bloodPressure, String immunizationRecord, String nurseNotes) {
        this.dateOfBirth = dateOfBirth;
        this.date = date;
        this.time = time;
        this.patientIdentifier = patientIdentifier;
        this.weight = weight;
        this.height = height;
        this.bodyTemp = bodyTemp;
        this.bloodPressure = bloodPressure;
        this.immunizationRecord = immunizationRecord;
        this.nurseNotes = nurseNotes;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(double bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(double bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getImmunizationRecord() {
        return immunizationRecord;
    }

    public void setImmunizationRecord(String immunizationRecord) {
        this.immunizationRecord = immunizationRecord;
    }

    public String getNurseNotes() {
        return nurseNotes;
    }

    public void setNurseNotes(String nurseNotes) {
        this.nurseNotes = nurseNotes;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDrNotes() {
        return drNotes;
    }

    public void setDrNotes(String drNotes) {
        this.drNotes = drNotes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    @Override
    public String toString() {
        return "PatientVisit{" +
                "date=" + dateOfBirth +
                ", time=" + time +
                ", patientIdentifier='" + patientIdentifier + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", bodyTemp=" + bodyTemp +
                ", bloodPressure=" + bloodPressure +
                ", nurseNotes='" + nurseNotes + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", drNotes='" + drNotes + '\'' +
                ", prescriptions=" + prescriptions +
                '}';
    }
}
