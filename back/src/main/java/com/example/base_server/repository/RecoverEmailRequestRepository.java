package com.example.base_server.repository;

import com.example.base_server.model.RecoverEmailRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecoverEmailRequestRepository extends JpaRepository<com.example.base_server.model.RecoverEmailRequest, Long> {

    Optional<RecoverEmailRequest> findByEmailIgnoreCase(String email);
    List<RecoverEmailRequest> findAllByResolvedFalseOrderByCreatedAtDesc();
}
