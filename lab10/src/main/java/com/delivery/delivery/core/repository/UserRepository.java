package com.delivery.delivery.core.repository;

import com.delivery.delivery.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByLogin(String login);
    void deleteUserById(long id);
    Optional<User> findBySub(String sub);
}
