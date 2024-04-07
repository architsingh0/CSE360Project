package com.solacecare.cse360project.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPatientIdentifier(String patientIdentifier);
    Optional<Patient> findByEmailAndPassword(String email, String password);
    Optional<Patient> findByEmail(String email);
}