package com.mrikh.homework_8.api.controller;

import com.mrikh.homework_8.api.dto.AuthRequest;
import com.mrikh.homework_8.api.dto.AuthResponse;
import com.mrikh.homework_8.api.dto.RegisterRequest;
import com.mrikh.homework_8.core.model.Customer;
import com.mrikh.homework_8.core.service.AuthService;
import com.mrikh.homework_8.core.service.CustomerService;
import com.mrikh.homework_8.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse createUser(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

}