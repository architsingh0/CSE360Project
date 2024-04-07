package com.solacecare.cse360project.nurse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
    Optional<Nurse> findByEmailAndPassword(String email, String password);
    Optional<Nurse> findByEmail(String email);
}