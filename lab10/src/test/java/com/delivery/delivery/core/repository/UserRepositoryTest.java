package com.delivery.delivery.core.repository;

import com.delivery.delivery.core.entity.User;
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
@Sql(scripts = "user_repository_test_resources.sql")
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void setup() {
        postgres.start();
    }

    @AfterAll
    static void cleanup() {
        postgres.stop();
    }

    @Test
    @DisplayName("Поиск пользователя по email - john.smith@example.com")
    void testFindByEmail_JohnSmith() {
        Optional<User> user = userRepository.findByEmail("john.smith@example.com");
        assertTrue(user.isPresent());
        assertEquals("john.smith@example.com", user.get().getEmail());
        assertEquals("johnsmith", user.get().getLogin());
        assertEquals("hashed_password_123", user.get().getPassword());
        assertEquals("USER", user.get().getRole());
        assertNotNull(user.get().getCustomer());
        assertEquals("John Smith", user.get().getCustomer().getName());
    }

    @Test
    @DisplayName("Поиск пользователя по email - несуществующий email")
    void testFindByEmail_NonExistentEmail() {
        Optional<User> user = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(user.isPresent());
    }

    @Test
    @DisplayName("Поиск пользователя по логину - alicebrown")
    void testFindByLogin_AliceBrown() {
        Optional<User> user = userRepository.findByLogin("alicebrown");
        assertTrue(user.isPresent());
        assertEquals("alice.brown@example.com", user.get().getEmail());
        assertEquals("alicebrown", user.get().getLogin());
        assertEquals("hashed_password_456", user.get().getPassword());
        assertEquals("USER", user.get().getRole());
        assertNotNull(user.get().getCustomer());
        assertEquals("Alice Brown", user.get().getCustomer().getName());
    }

    @Test
    @DisplayName("Поиск пользователя по логину - несуществующий логин")
    void testFindByLogin_NonExistentLogin() {
        Optional<User> user = userRepository.findByLogin("nonexistent");
        assertFalse(user.isPresent());
    }

    @Test
    @DisplayName("Удаление пользователя по ID - существующего пользователя")
    void testDeleteUserById_ExistingUser() {
        Optional<User> userBefore = userRepository.findById(3L); // Bob Jones
        assertTrue(userBefore.isPresent());

        userRepository.deleteUserById(3L);

        Optional<User> userAfter = userRepository.findById(3L);
        assertFalse(userAfter.isPresent());
    }

    @Test
    @DisplayName("Удаление пользователя по ID - несуществующего пользователя")
    void testDeleteUserById_NonExistentUser() {
        Optional<User> userBefore = userRepository.findById(999L);
        assertFalse(userBefore.isPresent());

        userRepository.deleteUserById(999L);

        Optional<User> userAfter = userRepository.findById(999L);
        assertFalse(userAfter.isPresent());
    }

    @Test
    @DisplayName("Удаление пользователя по ID - каскадное удаление связанных данных")
    void testDeleteUserById_CascadeDelete() {
        Optional<User> userBefore = userRepository.findById(1L); // John Smith
        assertTrue(userBefore.isPresent());
        assertNotNull(userBefore.get().getCustomer());

        userRepository.deleteUserById(1L);

        Optional<User> userAfter = userRepository.findById(1L);
        assertFalse(userAfter.isPresent());
    }
}
