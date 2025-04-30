package com.delivery.delivery.api.controller;

import com.delivery.delivery.api.dto.CustomerDto;
import com.delivery.delivery.api.dto.UpdateUserDto;
import com.delivery.delivery.api.dto.UserDto;
import com.delivery.delivery.core.entity.User;
import com.delivery.delivery.core.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Long createUser( @RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UpdateUserDto updateUser(Authentication authentication, @RequestBody UpdateUserDto preEditUser) {
        return userService.updateUser(authentication, preEditUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Authentication authentication, @PathVariable long id) {
        userService.deleteUser(authentication, id);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public User getUser(Authentication authentication, @PathVariable long id) {
        return userService.getUserById(authentication, id);
    }

    @PatchMapping("/customer")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CustomerDto updateCustomer(Authentication authentication, @RequestBody CustomerDto customerDto) {
        return userService.updateCustomer(authentication, customerDto);
    }
}