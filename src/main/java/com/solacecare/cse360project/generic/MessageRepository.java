package com.solacecare.cse360project.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderEmail(String email);

    List<Message> findByRecipientEmail(String email);

    Optional<Message> findById(Long id);
}