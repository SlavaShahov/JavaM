package com.shakhov.homework_9.api.controller;

import com.shakhov.homework_9.api.dto.AuthRequest;
import com.shakhov.homework_9.api.dto.AuthResponse;
import com.shakhov.homework_9.api.dto.RegisterRequest;
import com.shakhov.homework_9.core.model.Customer;
import com.shakhov.homework_9.core.service.AuthService;
import com.shakhov.homework_9.core.service.CustomerService;
import com.shakhov.homework_9.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUser(@RequestBody RegisterRequest request) {
        Customer customer = customerService.createCustomer(request.getName(), request.getAddress());
        userService.createCustomerUser(request.getUsername(), request.getPassword(), customer);
        return authService.authenticateUser(request.getUsername(), request.getPassword())
                .map(user -> {
                    String token = authService.createToken(user);
                    return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


        @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return authService.authenticateUser(request.getUsername(), request.getPassword())
                .map(user -> {
                    String token = authService.createToken(user);
                    return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            authService.invalidateToken(token);
        }
        return ResponseEntity.ok().build();
    }
}