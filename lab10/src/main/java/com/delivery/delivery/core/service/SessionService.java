package com.delivery.delivery.core.service;


import com.delivery.delivery.api.dto.AuthorizationDto;
import com.delivery.delivery.core.entity.Session;
import com.delivery.delivery.core.entity.User;
import com.delivery.delivery.core.exception.NotFoundException;
import com.delivery.delivery.core.repository.SessionRepository;
import com.delivery.delivery.core.repository.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class SessionService {

    private final RestTemplate restTemplate;

    public String createSession(AuthorizationDto body) {
        if (body.login() == null || body.password() == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "password");
        requestBody.add("client_id", "my-app");
        requestBody.add("client_secret", "kAlMQFu1c4jYRmGzZSPwrZKmsgfZ50Z3");
        requestBody.add("username", body.login());
        requestBody.add("password", body.password());
        requestBody.add("scope", "openid profile email");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            String url = "http://localhost:8080/realms/my-realm/protocol/openid-connect/token";
            TokenResponse response = restTemplate.postForObject(url, request, TokenResponse.class);

            if (response == null || response.getAccessToken() == null) {
                throw new RuntimeException("Failed to obtain token from Keycloak");
            }

            log.info("Successfully obtained token for user: {}", body.login());
            return response.getAccessToken();
        } catch (Exception e) {
            log.error("Error obtaining token from Keycloak: {}", e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    private static class TokenResponse {
        private String access_token;
        private String refresh_token;
        private String token_type;
        private int expires_in;
        private int refresh_expires_in;
        private String scope;

        public String getAccessToken() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public int getRefresh_expires_in() {
            return refresh_expires_in;
        }

        public void setRefresh_expires_in(int refresh_expires_in) {
            this.refresh_expires_in = refresh_expires_in;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }
}