package com.delivery.delivery.core.repository;

import com.delivery.delivery.core.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Optional<Session> findByToken(String token);
    void deleteById(Long id);
}
