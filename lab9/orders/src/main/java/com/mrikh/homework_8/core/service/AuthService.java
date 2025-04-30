package com.mrikh.homework_8.core.service;

import com.mrikh.homework_8.api.dto.AuthRequest;
import com.mrikh.homework_8.api.dto.AuthResponse;
import com.mrikh.homework_8.api.dto.RegisterRequest;
import com.mrikh.homework_8.core.model.AuthToken;
import com.mrikh.homework_8.core.model.User;
import com.mrikh.homework_8.core.repository.AuthTokenRepository;
import com.mrikh.homework_8.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final AuthTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    private String userServiceUrl = "http://localhost:8083";

    public AuthResponse register(RegisterRequest request) {
        String url = userServiceUrl + "/auth/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterRequest> httpRequest = new HttpEntity<>(request, headers);
        ResponseEntity<AuthResponse> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest, AuthResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new RuntimeException("Registration failed: Unauthorized");
        } else {
            throw new RuntimeException("Registration failed: " + response.getStatusCode());
        }
    }

    public AuthResponse login(AuthRequest request) {
        String url = userServiceUrl + "/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequest> httpRequest = new HttpEntity<>(request, headers);
        ResponseEntity<AuthResponse> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest, AuthResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new RuntimeException("Login failed: Unauthorized");
        } else {
            throw new RuntimeException("Login failed: " + response.getStatusCode());
        }
    }

    public void logout(String token) {
        String url = userServiceUrl + "/auth/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, request, Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Logout failed: " + response.getStatusCode());
        }
    }

}