package com.delivery.delivery.core.repository;

import com.delivery.delivery.core.entity.BannedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<BannedToken, String> {
    Optional<BannedToken> findByToken(String token);
}
