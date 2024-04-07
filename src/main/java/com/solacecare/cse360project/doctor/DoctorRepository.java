package com.solacecare.cse360project.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmailAndPassword(String email, String password);

    Optional<Doctor> findByEmail(String email);
}