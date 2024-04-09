package com.solacecare.cse360project.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findAllByPatientVisitPatientIdentifier(String patientIdentifier);
}