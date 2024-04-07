package com.solacecare.cse360project.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientVisitRepository patientVisitRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, PatientVisitRepository patientVisitRepository) {
        this.patientRepository = patientRepository;
        this.patientVisitRepository = patientVisitRepository;
    }

    @Transactional
    public void associateVisitsWithPatient(String patientIdentifier) {
        Optional<Patient> patientOpt = patientRepository.findByPatientIdentifier(patientIdentifier);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            List<PatientVisit> existingVisits = patient.getVisits();
            existingVisits.clear();
            List<PatientVisit> newVisits = patientVisitRepository.findByPatientIdentifier(patientIdentifier);
            existingVisits.addAll(newVisits);
            newVisits.forEach(visit -> {
                if (visit.getPatient() != patient) {
                    visit.setPatient(patient);
                }
            });
            patientRepository.save(patient);
        } else {
            throw new RuntimeException("Patient not found with identifier: " + patientIdentifier);
        }
    }
}
