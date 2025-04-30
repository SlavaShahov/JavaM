package com.mrikh.homework_8.core.service;

import com.mrikh.homework_8.core.model.AuthToken;
import com.mrikh.homework_8.core.model.User;
import com.mrikh.homework_8.core.model.UserRole;
import com.mrikh.homework_8.core.repository.AuthTokenRepository;
import com.mrikh.homework_8.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AuthTokenRepository tokenRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    private User user;
    private AuthToken authToken;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encoded_password");
        user.setRole(UserRole.ROLE_ADMIN);

        authToken = new AuthToken();
        authToken.setToken(UUID.randomUUID().toString());
        authToken.setUser(user);
        authToken.setExpiryDate(LocalDateTime.now().plusHours(3));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    }

    @Test
    @DisplayName("Аутентификация пользователя - Успешный случай")
    void testAuthenticateUser_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(true);

        Optional<User> result = authService.authenticateUser("testuser", "password123");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("password123", "encoded_password");
    }

    @Test
    @DisplayName("Аутентификация пользователя - Пользователь не найден")
    void testAuthenticateUser_UserNotFound() {
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        Optional<User> result = authService.authenticateUser("unknownuser", "password123");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername("unknownuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Аутентификация пользователя - Неверный пароль")
    void testAuthenticateUser_WrongPassword() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encoded_password")).thenReturn(false);

        Optional<User> result = authService.authenticateUser("testuser", "wrongpassword");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("wrongpassword", "encoded_password");
    }

    @Test
    @DisplayName("Валидация токена - Успешный случай")
    void testValidateToken_Success() {
        when(tokenRepository.findByToken(authToken.getToken())).thenReturn(Optional.of(authToken));

        Optional<User> result = authService.validateToken(authToken.getToken());

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(tokenRepository, times(1)).findByToken(authToken.getToken());
    }

    @Test
    @DisplayName("Валидация токена - Токен не найден")
    void testValidateToken_TokenNotFound() {
        when(tokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());

        Optional<User> result = authService.validateToken("invalid-token");

        assertFalse(result.isPresent());
        verify(tokenRepository, times(1)).findByToken("invalid-token");
    }

    @Test
    @DisplayName("Валидация токена - Токен истёк")
    void testValidateToken_TokenExpired() {
        authToken.setExpiryDate(LocalDateTime.now().minusHours(1)); // Токен истёк
        when(tokenRepository.findByToken(authToken.getToken())).thenReturn(Optional.of(authToken));

        Optional<User> result = authService.validateToken(authToken.getToken());

        assertFalse(result.isPresent());
        verify(tokenRepository, times(1)).findByToken(authToken.getToken());
    }

    @Test
    @DisplayName("Аннулирование токена - Успешный случай")
    @Transactional
    void testInvalidateToken_Success() {
        when(tokenRepository.findByToken(authToken.getToken())).thenReturn(Optional.of(authToken));
        doNothing().when(tokenRepository).delete(authToken);

        authService.invalidateToken(authToken.getToken());

        verify(tokenRepository, times(1)).findByToken(authToken.getToken());
        verify(tokenRepository, times(1)).delete(authToken);
    }

    @Test
    @DisplayName("Аннулирование токена - Токен не найден")
    @Transactional
    void testInvalidateToken_TokenNotFound() {
        when(tokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());

        authService.invalidateToken("invalid-token");

        verify(tokenRepository, times(1)).findByToken("invalid-token");
        verify(tokenRepository, never()).delete(any(AuthToken.class));
    }
}
