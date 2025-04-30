package com.shakhov.homework_9.core.service;

import com.shakhov.homework_9.core.model.AuthToken;
import com.shakhov.homework_9.core.model.User;
import com.shakhov.homework_9.core.repository.AuthTokenRepository;
import com.shakhov.homework_9.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> authenticateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Transactional
    public String createToken(User user) {
        tokenRepository.deleteByUser_Id(user.getId());

        AuthToken token = new AuthToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusHours(3));

        tokenRepository.save(token);
        return token.getToken();
    }

    public Optional<User> validateToken(String tokenValue) {
        return tokenRepository.findByToken(tokenValue)
                .filter(token -> !token.isExpired())
                .map(AuthToken::getUser);
    }

    @Transactional
    public void invalidateToken(String tokenValue) {
        tokenRepository.findByToken(tokenValue)
                .ifPresent(tokenRepository::delete);
    }
}