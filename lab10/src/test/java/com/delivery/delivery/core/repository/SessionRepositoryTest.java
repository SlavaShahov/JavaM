package com.delivery.delivery.core.repository;

import com.delivery.delivery.core.entity.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Sql(scripts = "token_session_repository_test_resources.sql")
class SessionRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeAll
    static void setup() {
        postgres.start();
    }

    @AfterAll
    static void cleanup() {
        postgres.stop();
    }

    @Test
    @DisplayName("Поиск сессии по токену - token_johnsmith_123")
    void testFindByToken_ExistingToken() {
        Optional<Session> session = sessionRepository.findByToken("token_johnsmith_123");
        assertTrue(session.isPresent());
        assertEquals("token_johnsmith_123", session.get().getToken());
        assertEquals(1L, session.get().getUserId());
    }

    @Test
    @DisplayName("Поиск сессии по токену - несуществующий токен")
    void testFindByToken_NonExistentToken() {
        Optional<Session> session = sessionRepository.findByToken("nonexistent_token");
        assertFalse(session.isPresent());
    }

    @Test
    @DisplayName("Удаление сессии по ID - существующая сессия")
    void testDeleteById_ExistingSession() {
        Optional<Session> sessionBefore = sessionRepository.findById("3");
        assertTrue(sessionBefore.isPresent());

        sessionRepository.deleteById(3L);

        Optional<Session> sessionAfter = sessionRepository.findById("3");
        assertFalse(sessionAfter.isPresent());
    }

    @Test
    @DisplayName("Удаление сессии по ID - несуществующая сессия")
    void testDeleteById_NonExistentSession() {
        Optional<Session> sessionBefore = sessionRepository.findById("999");
        assertFalse(sessionBefore.isPresent());

        sessionRepository.deleteById(999L);

        Optional<Session> sessionAfter = sessionRepository.findById("999");
        assertFalse(sessionAfter.isPresent());
    }
}
