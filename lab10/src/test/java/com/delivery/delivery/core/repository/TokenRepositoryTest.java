package com.delivery.delivery.core.repository;

import com.delivery.delivery.core.entity.BannedToken;
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
class TokenRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TokenRepository tokenRepository;

    @BeforeAll
    static void setup() {
        postgres.start();
    }

    @AfterAll
    static void cleanup() {
        postgres.stop();
    }

    @Test
    @DisplayName("Поиск заблокированного токена - invalid_token_123")
    void testFindByToken_ExistingToken() {
        Optional<BannedToken> token = tokenRepository.findByToken("invalid_token_123");
        assertTrue(token.isPresent());
        assertEquals("invalid_token_123", token.get().getToken());
    }

    @Test
    @DisplayName("Поиск заблокированного токена - несуществующий токен")
    void testFindByToken_NonExistentToken() {
        Optional<BannedToken> token = tokenRepository.findByToken("nonexistent_token");
        assertFalse(token.isPresent());
    }
}
